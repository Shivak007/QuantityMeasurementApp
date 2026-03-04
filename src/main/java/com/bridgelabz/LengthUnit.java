package com.bridgelabz;

/**
 * Standalone LengthUnit enum.
 * Responsible for all conversion logic.
 * Base unit: FEET
 */
public enum LengthUnit {

    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private final double toFeetFactor;

    LengthUnit(double toFeetFactor) {
        this.toFeetFactor = toFeetFactor;
    }

    // Converts value in this unit to base unit (feet)
    public double convertToBaseUnit(double value) {
        return value * toFeetFactor;
    }

    // Converts value from base unit (feet) to this unit
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / toFeetFactor;
    }

    public double getConversionFactor() {
        return toFeetFactor;
    }
}