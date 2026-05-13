package com.app.quantitymeasurement.unit;

/**
 * LengthUnit - Enum representing supported length units.
 * Base unit: INCH
 * Preserved unchanged from UC16.
 */
public enum LengthUnit implements IMeasurable {
    INCH(1.0, "INCH"),
    FEET(12.0, "INCH"),
    YARD(36.0, "INCH"),
    CENTIMETER(0.393701, "INCH");

    private final double conversionFactor;
    private final String baseUnit;

    LengthUnit(double conversionFactor, String baseUnit) {
        this.conversionFactor = conversionFactor;
        this.baseUnit = baseUnit;
    }

    @Override
    public double getConversionFactor() { return conversionFactor; }

    @Override
    public String getBaseUnit() { return baseUnit; }
}
