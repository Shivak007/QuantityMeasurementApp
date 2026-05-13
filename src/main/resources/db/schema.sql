-- ============================================================
-- Quantity Measurement Application - Database Schema
-- UC16: JDBC Database Integration
-- ============================================================

-- Drop tables if they exist (for clean re-initialization)
DROP TABLE IF EXISTS quantity_measurement_history;
DROP TABLE IF EXISTS quantity_measurement_entity;

-- ============================================================
-- Main entity table for quantity measurement operations
-- ============================================================
CREATE TABLE quantity_measurement_entity (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    operation_type      VARCHAR(50)     NOT NULL,
    measurement_type    VARCHAR(50)     NOT NULL,
    first_value         DOUBLE          NOT NULL,
    first_unit          VARCHAR(50)     NOT NULL,
    second_value        DOUBLE,
    second_unit         VARCHAR(50),
    result              DOUBLE,
    result_boolean      BOOLEAN,
    created_at          TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

-- Index for faster lookups by operation type
CREATE INDEX idx_operation_type ON quantity_measurement_entity(operation_type);

-- Index for faster lookups by measurement type
CREATE INDEX idx_measurement_type ON quantity_measurement_entity(measurement_type);

-- Index for time-based queries
CREATE INDEX idx_created_at ON quantity_measurement_entity(created_at);

-- ============================================================
-- Audit / History table for tracking all operations
-- ============================================================
CREATE TABLE quantity_measurement_history (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_id           BIGINT,
    operation_type      VARCHAR(50)     NOT NULL,
    measurement_type    VARCHAR(50)     NOT NULL,
    description         VARCHAR(500),
    performed_at        TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (entity_id) REFERENCES quantity_measurement_entity(id) ON DELETE SET NULL
);

-- Index for history lookups
CREATE INDEX idx_history_entity_id ON quantity_measurement_history(entity_id);
CREATE INDEX idx_history_performed_at ON quantity_measurement_history(performed_at);
