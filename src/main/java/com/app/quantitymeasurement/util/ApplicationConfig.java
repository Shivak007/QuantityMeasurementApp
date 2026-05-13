package com.app.quantitymeasurement.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private static final String PROPERTIES_FILE = "application.properties";

    private static ApplicationConfig instance;
    private final Properties properties;

    private ApplicationConfig() {
        properties = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (is != null) {
                properties.load(is);
                logger.info("ApplicationConfig: Loaded configuration from {}", PROPERTIES_FILE);
            } else {
                logger.warn("ApplicationConfig: {} not found; using defaults.", PROPERTIES_FILE);
            }
        } catch (IOException e) {
            logger.error("ApplicationConfig: Failed to load {}: {}", PROPERTIES_FILE, e.getMessage());
        }
    }

    public static synchronized ApplicationConfig getInstance() {
        if (instance == null) {
            instance = new ApplicationConfig();
        }
        return instance;
    }

    public String getProperty(String key, String defaultValue) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isEmpty()) {
            return systemValue;
        }
        return properties.getProperty(key, defaultValue);
    }

    public String getProperty(String key) {
        return getProperty(key, null);
    }

    public int getIntProperty(String key, int defaultValue) {
        String val = getProperty(key);
        if (val == null) return defaultValue;

        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer for '{}', using default {}", key, defaultValue);
            return defaultValue;
        }
    }

    public long getLongProperty(String key, long defaultValue) {
        String val = getProperty(key);
        if (val == null) return defaultValue;

        try {
            return Long.parseLong(val.trim());
        } catch (NumberFormatException e) {
            logger.warn("Invalid long for '{}', using default {}", key, defaultValue);
            return defaultValue;
        }
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String val = getProperty(key);
        if (val == null) return defaultValue;
        return Boolean.parseBoolean(val.trim());
    }

    public String getRepositoryType() {
        return getProperty("repository.type", "cache");
    }

    public String getDbDriver() {
        return getProperty("db.driver", "org.h2.Driver");
    }

    public String getDbUrl() {
        return getProperty("db.url",
                "jdbc:h2:mem:quantitydb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
    }

    public String getDbUsername() {
        return getProperty("db.username", "sa");
    }

    public String getDbPassword() {
        return getProperty("db.password", "");
    }

    public int getPoolInitialSize() {
        return getIntProperty("db.pool.initialSize", 5);
    }

    public int getPoolMaxSize() {
        return getIntProperty("db.pool.maxSize", 10);
    }

    public long getConnectionTimeout() {
        return getLongProperty("db.pool.connectionTimeout", 30000L);
    }

    public long getIdleTimeout() {
        return getLongProperty("db.pool.idleTimeout", 600000L);
    }

    // For testing
    static synchronized void reset() {
        instance = null;
    }
}