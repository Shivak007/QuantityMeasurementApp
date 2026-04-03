package com.bridgelabz.model;

import java.io.Serializable;

public class QuantityMeasurementEntity implements Serializable {

    private QuantityDTO input1;
    private QuantityDTO input2;
    private QuantityDTO result;
    private String operation;
    private String error;

    public QuantityMeasurementEntity(QuantityDTO input1, QuantityDTO input2,
                                     QuantityDTO result, String operation) {
        this.input1 = input1;
        this.input2 = input2;
        this.result = result;
        this.operation = operation;
    }

    public QuantityMeasurementEntity(String error) {
        this.error = error;
    }

    public boolean hasError() {
        return error != null;
    }

    @Override
    public String toString() {
        if (hasError()) return "Error: " + error;
        return operation + " -> " + result.getValue() + " " + result.getUnit();
    }
}