package com.bridgelabz.model;

public class QuantityDTO {

    private double value;
    private String unit;

    public QuantityDTO(double value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }
}