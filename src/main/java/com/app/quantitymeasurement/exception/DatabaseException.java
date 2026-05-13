package com.app.quantitymeasurement.exception;

/**
 * DatabaseException - Custom exception for database-related errors.
 * UC16: Wraps JDBC and SQL exceptions with meaningful context messages
 * so upper layers receive clear, actionable error information without
 * direct coupling to JDBC internals.
 */
public class DatabaseException extends QuantityMeasurementException {

    private final String operation;

    public DatabaseException(String message) {
        super(message);
        this.operation = "UNKNOWN";
    }

    public DatabaseException(String operation, String message) {
        super("[DB:" + operation + "] " + message);
        this.operation = operation;
    }

    public DatabaseException(String operation, String message, Throwable cause) {
        super("[DB:" + operation + "] " + message, cause);
        this.operation = operation;
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
        this.operation = "UNKNOWN";
    }

    public String getOperation() {
        return operation;
    }
}
