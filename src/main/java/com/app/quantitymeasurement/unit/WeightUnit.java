package com.app.quantitymeasurement.unit;

/**
 * WeightUnit - Enum representing supported weight units.
 * Base unit: GRAM
 */
public enum WeightUnit implements IMeasurable {
    GRAM(1.0, "GRAM"),
    KILOGRAM(1000.0, "GRAM"),
    TONNE(1000000.0, "GRAM");

    private final double conversionFactor;
    private final String baseUnit;

    WeightUnit(double conversionFactor, String baseUnit) {
        this.conversionFactor = conversionFactor;
        this.baseUnit = baseUnit;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public String getBaseUnit() {
        return baseUnit;
    }
}
