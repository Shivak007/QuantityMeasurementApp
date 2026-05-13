package com.app.quantitymeasurement.unit;

/**
 * IMeasurable - Interface for all measurement unit enums.
 * Each unit must provide its conversion factor relative to a base unit
 * and identify the base unit name.
 */
public interface IMeasurable {
    double getConversionFactor();
    String getBaseUnit();
}
