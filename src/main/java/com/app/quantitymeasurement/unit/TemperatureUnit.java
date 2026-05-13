package com.app.quantitymeasurement.unit;

/**
 * TemperatureUnit - Enum representing supported temperature units.
 * Note: Temperature conversion requires special formula (not just multiplication).
 * Base unit: CELSIUS
 * Preserved unchanged from UC16.
 */
public enum TemperatureUnit implements IMeasurable {
    CELSIUS(1.0, "CELSIUS"),
    FAHRENHEIT(1.0, "CELSIUS"),  // Conversion handled separately: (F - 32) * 5/9
    KELVIN(1.0, "CELSIUS");      // Conversion handled separately: K - 273.15

    private final double conversionFactor;
    private final String baseUnit;

    TemperatureUnit(double conversionFactor, String baseUnit) {
        this.conversionFactor = conversionFactor;
        this.baseUnit = baseUnit;
    }

    @Override
    public double getConversionFactor() { return conversionFactor; }

    @Override
    public String getBaseUnit() { return baseUnit; }

    /**
     * Converts a value from this unit to Celsius.
     */
    public double toCelsius(double value) {
        switch (this) {
            case FAHRENHEIT: return (value - 32.0) * 5.0 / 9.0;
            case KELVIN:     return value - 273.15;
            default:         return value;
        }
    }
}
