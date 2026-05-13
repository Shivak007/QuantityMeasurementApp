package com.app.quantitymeasurement.exception;

/**
 * QuantityMeasurementException - Base custom exception for all
 * application-level errors in the Quantity Measurement App.
 */
public class QuantityMeasurementException extends RuntimeException {

    public QuantityMeasurementException(String message) {
        super(message);
    }

    public QuantityMeasurementException(String message, Throwable cause) {
        super(message, cause);
    }
}
