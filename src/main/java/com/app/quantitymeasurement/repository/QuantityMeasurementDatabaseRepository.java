package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.DatabaseException;
import com.app.quantitymeasurement.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * QuantityMeasurementDatabaseRepository - JDBC implementation of
 * IQuantityMeasurementRepository that persists entities to a relational
 * database using parameterized queries and connection pooling.
 *
 * UC16: Database Integration with JDBC
 */
public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementDatabaseRepository.class);

    // ---- SQL Statements ----
    private static final String SQL_INSERT =
        "INSERT INTO quantity_measurement_entity " +
        "(operation_type, measurement_type, first_value, first_unit, second_value, second_unit, result, result_boolean, created_at) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_ALL =
        "SELECT id, operation_type, measurement_type, first_value, first_unit, " +
        "second_value, second_unit, result, result_boolean, created_at " +
        "FROM quantity_measurement_entity ORDER BY id";

    private static final String SQL_SELECT_BY_OPERATION =
        "SELECT id, operation_type, measurement_type, first_value, first_unit, " +
        "second_value, second_unit, result, result_boolean, created_at " +
        "FROM quantity_measurement_entity WHERE UPPER(operation_type) = UPPER(?) ORDER BY id";

    private static final String SQL_SELECT_BY_TYPE =
        "SELECT id, operation_type, measurement_type, first_value, first_unit, " +
        "second_value, second_unit, result, result_boolean, created_at " +
        "FROM quantity_measurement_entity WHERE UPPER(measurement_type) = UPPER(?) ORDER BY id";

    private static final String SQL_COUNT =
        "SELECT COUNT(*) FROM quantity_measurement_entity";

    private static final String SQL_DELETE_ALL =
        "DELETE FROM quantity_measurement_entity";

    private final ConnectionPool connectionPool;

    public QuantityMeasurementDatabaseRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        logger.info("QuantityMeasurementDatabaseRepository: Initialised with pool: {}",
            connectionPool.getPoolStatistics());
    }

    // ---- SAVE ----

    @Override
    public QuantityMeasurementEntity save(QuantityMeasurementEntity entity) {
        Connection conn = null;
        try {
            conn = connectionPool.acquireConnection();
            conn.setAutoCommit(false);  // Begin transaction

            try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, entity.getOperationType());
                ps.setString(2, entity.getMeasurementType());
                ps.setDouble(3, entity.getFirstValue());
                ps.setString(4, entity.getFirstUnit());
                ps.setDouble(5, entity.getSecondValue());
                ps.setString(6, entity.getSecondUnit());
                ps.setDouble(7, entity.getResult());
                ps.setBoolean(8, entity.isResultBoolean());
                ps.setTimestamp(9, Timestamp.valueOf(
                    entity.getCreatedAt() != null ? entity.getCreatedAt() : LocalDateTime.now()));

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DatabaseException("SAVE", "Insert returned 0 rows affected.");
                }

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getLong(1));
                    }
                }
            }

            conn.commit();
            logger.debug("DatabaseRepository: Saved entity id={} op={} type={}",
                entity.getId(), entity.getOperationType(), entity.getMeasurementType());
            return entity;

        } catch (SQLException e) {
            rollbackQuietly(conn);
            throw new DatabaseException("SAVE", "Failed to save entity: " + e.getMessage(), e);
        } finally {
            resetAutoCommit(conn);
            connectionPool.releaseConnection(conn);
        }
    }

    // ---- GET ALL ----

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        Connection conn = null;
        try {
            conn = connectionPool.acquireConnection();
            try (PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
                 ResultSet rs = ps.executeQuery()) {
                List<QuantityMeasurementEntity> results = mapResultSet(rs);
                logger.debug("DatabaseRepository: Retrieved {} measurements.", results.size());
                return results;
            }
        } catch (SQLException e) {
            throw new DatabaseException("SELECT_ALL", "Failed to retrieve measurements: " + e.getMessage(), e);
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }

    // ---- GET BY OPERATION ----

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operationType) {
        Connection conn = null;
        try {
            conn = connectionPool.acquireConnection();
            try (PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_OPERATION)) {
                ps.setString(1, operationType);
                try (ResultSet rs = ps.executeQuery()) {
                    List<QuantityMeasurementEntity> results = mapResultSet(rs);
                    logger.debug("DatabaseRepository: Found {} entities for operation '{}'.",
                        results.size(), operationType);
                    return results;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("SELECT_BY_OP",
                "Failed to query by operation '" + operationType + "': " + e.getMessage(), e);
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }

    // ---- GET BY TYPE ----

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType) {
        Connection conn = null;
        try {
            conn = connectionPool.acquireConnection();
            try (PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_TYPE)) {
                ps.setString(1, measurementType);
                try (ResultSet rs = ps.executeQuery()) {
                    List<QuantityMeasurementEntity> results = mapResultSet(rs);
                    logger.debug("DatabaseRepository: Found {} entities for type '{}'.",
                        results.size(), measurementType);
                    return results;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("SELECT_BY_TYPE",
                "Failed to query by type '" + measurementType + "': " + e.getMessage(), e);
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }

    // ---- COUNT ----

    @Override
    public int getTotalCount() {
        Connection conn = null;
        try {
            conn = connectionPool.acquireConnection();
            try (PreparedStatement ps = conn.prepareStatement(SQL_COUNT);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    logger.debug("DatabaseRepository: Total count = {}", count);
                    return count;
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("COUNT", "Failed to count measurements: " + e.getMessage(), e);
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }

    // ---- DELETE ALL ----

    @Override
    public void deleteAll() {
        Connection conn = null;
        try {
            conn = connectionPool.acquireConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(SQL_DELETE_ALL)) {
                int deleted = ps.executeUpdate();
                conn.commit();
                logger.info("DatabaseRepository: Deleted {} measurement records.", deleted);
            }
        } catch (SQLException e) {
            rollbackQuietly(conn);
            throw new DatabaseException("DELETE_ALL", "Failed to delete measurements: " + e.getMessage(), e);
        } finally {
            resetAutoCommit(conn);
            connectionPool.releaseConnection(conn);
        }
    }

    // ---- Lifecycle ----

    @Override
    public String getPoolStatistics() {
        return connectionPool.getPoolStatistics();
    }

    @Override
    public void releaseResources() {
        connectionPool.shutdown();
        logger.info("DatabaseRepository: Connection pool shut down.");
    }

    // ---- Private Helpers ----

    /**
     * Maps all rows in a ResultSet to a list of QuantityMeasurementEntity objects.
     */
    private List<QuantityMeasurementEntity> mapResultSet(ResultSet rs) throws SQLException {
        List<QuantityMeasurementEntity> list = new ArrayList<>();
        while (rs.next()) {
            QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
            entity.setId(rs.getLong("id"));
            entity.setOperationType(rs.getString("operation_type"));
            entity.setMeasurementType(rs.getString("measurement_type"));
            entity.setFirstValue(rs.getDouble("first_value"));
            entity.setFirstUnit(rs.getString("first_unit"));
            entity.setSecondValue(rs.getDouble("second_value"));
            entity.setSecondUnit(rs.getString("second_unit"));
            entity.setResult(rs.getDouble("result"));
            entity.setResultBoolean(rs.getBoolean("result_boolean"));
            Timestamp ts = rs.getTimestamp("created_at");
            entity.setCreatedAt(ts != null ? ts.toLocalDateTime() : LocalDateTime.now());
            list.add(entity);
        }
        return list;
    }

    private void rollbackQuietly(Connection conn) {
        if (conn == null) return;
        try {
            conn.rollback();
            logger.warn("DatabaseRepository: Transaction rolled back.");
        } catch (SQLException ex) {
            logger.error("DatabaseRepository: Rollback failed: {}", ex.getMessage());
        }
    }

    private void resetAutoCommit(Connection conn) {
        if (conn == null) return;
        try {
            conn.setAutoCommit(true);
        } catch (SQLException ex) {
            logger.warn("DatabaseRepository: Could not reset auto-commit: {}", ex.getMessage());
        }
    }
}
