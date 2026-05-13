package com.app.quantitymeasurement.unit;

/**
 * VolumeUnit - Enum representing supported volume units.
 * Base unit: LITRE
 */
public enum VolumeUnit implements IMeasurable {
    LITRE(1.0, "LITRE"),
    GALLON(3.78541, "LITRE"),
    ML(0.001, "LITRE");

    private final double conversionFactor;
    private final String baseUnit;

    VolumeUnit(double conversionFactor, String baseUnit) {
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
