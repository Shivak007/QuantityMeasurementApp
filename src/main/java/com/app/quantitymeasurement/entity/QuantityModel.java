package com.app.quantitymeasurement.entity;

import com.app.quantitymeasurement.unit.IMeasurable;

/**
 * QuantityModel - Represents a quantity with a value and a unit.
 * Used internally by the service layer for business logic.
 */
public class QuantityModel {

    private final double value;
    private final IMeasurable unit;

    public QuantityModel(double value, IMeasurable unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public IMeasurable getUnit() {
        return unit;
    }

    /**
     * Converts the quantity value to the base unit value.
     */
    public double toBaseUnit() {
        return value * unit.getConversionFactor();
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }
}
