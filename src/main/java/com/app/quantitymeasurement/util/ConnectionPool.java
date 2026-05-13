package com.app.quantitymeasurement.util;

import com.app.quantitymeasurement.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);

    private final BlockingQueue<Connection> availableConnections;
    private final List<Connection> allConnections;
    private final ApplicationConfig config;
    private final AtomicInteger activeCount;
    private volatile boolean closed = false;

    private static ConnectionPool instance;

    private ConnectionPool(ApplicationConfig config) {
        this.config = config;
        int maxSize = config.getPoolMaxSize();
        int initialSize = config.getPoolInitialSize();

        availableConnections = new ArrayBlockingQueue<>(maxSize);
        allConnections = new ArrayList<>(maxSize);
        activeCount = new AtomicInteger(0);

        logger.info("ConnectionPool: Initialising with {} initial / {} max connections.", initialSize, maxSize);
        initializePool(initialSize);
        logger.info("ConnectionPool: Initialised. {} connections ready.", availableConnections.size());
    }

    public static synchronized ConnectionPool getInstance() {
        ApplicationConfig cfg = ApplicationConfig.getInstance();
        if (instance == null || instance.closed) {
            instance = new ConnectionPool(cfg);
        }
        return instance;
    }

    // For testing
    public static ConnectionPool createPool(ApplicationConfig config) {
        return new ConnectionPool(config);
    }

    private void initializePool(int count) {
        for (int i = 0; i < count; i++) {
            try {
                Connection conn = createConnection();
                availableConnections.offer(conn);
                allConnections.add(conn);
            } catch (DatabaseException e) {
                logger.error("ConnectionPool: Failed to create initial connection #{}: {}", i + 1, e.getMessage());
            }
        }
    }

    private Connection createConnection() {
        try {
            Class.forName(config.getDbDriver());
            Connection conn = DriverManager.getConnection(
                    config.getDbUrl(),
                    config.getDbUsername(),
                    config.getDbPassword()
            );
            logger.debug("ConnectionPool: New connection created: {}", conn);
            return conn;
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("INIT", "JDBC driver not found: " + config.getDbDriver(), e);
        } catch (SQLException e) {
            throw new DatabaseException("INIT", "Failed to create DB connection: " + e.getMessage(), e);
        }
    }

    public Connection acquireConnection() {
        if (closed) {
            throw new DatabaseException("ACQUIRE", "Connection pool is closed.");
        }

        try {
            long timeout = config.getConnectionTimeout();
            Connection conn = availableConnections.poll(timeout, TimeUnit.MILLISECONDS);

            if (conn == null) {
                if (allConnections.size() < config.getPoolMaxSize()) {
                    conn = createConnection();
                    allConnections.add(conn);
                    logger.info("ConnectionPool: Pool grown to {} connections.", allConnections.size());
                } else {
                    throw new DatabaseException(
                            "ACQUIRE",
                            "Connection pool exhausted. All " + config.getPoolMaxSize() + " connections in use."
                    );
                }
            }

            activeCount.incrementAndGet();

            logger.debug("ConnectionPool: Connection acquired. Active={}, Available={}",
                    activeCount.get(), availableConnections.size());

            return conn;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DatabaseException("ACQUIRE", "Interrupted while waiting for connection.", e);
        }
    }

    public void releaseConnection(Connection conn) {
        if (conn == null) return;

        try {
            if (!conn.isClosed() && !closed) {
                availableConnections.offer(conn);
                activeCount.decrementAndGet();

                logger.debug("ConnectionPool: Connection released. Active={}, Available={}",
                        activeCount.get(), availableConnections.size());
            }
        } catch (SQLException e) {
            logger.warn("ConnectionPool: Could not check connection state on release: {}", e.getMessage());
        }
    }

    public synchronized void shutdown() {
        if (closed) return;

        closed = true;

        logger.info("ConnectionPool: Shutting down. Closing {} connections.", allConnections.size());

        for (Connection conn : allConnections) {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.warn("ConnectionPool: Error closing connection: {}", e.getMessage());
            }
        }

        allConnections.clear();
        availableConnections.clear();
        instance = null;

        logger.info("ConnectionPool: Shutdown complete.");
    }

    public int getAvailableCount() {
        return availableConnections.size();
    }

    public int getActiveCount() {
        return activeCount.get();
    }

    public int getTotalCount() {
        return allConnections.size();
    }

    public boolean isClosed() {
        return closed;
    }

    // Pool stats
    public String getPoolStatistics() {
        return String.format(
                "ConnectionPool[total=%d, active=%d, available=%d, maxSize=%d, closed=%b]",
                getTotalCount(),
                getActiveCount(),
                getAvailableCount(),
                config.getPoolMaxSize(),
                closed
        );
    }
}