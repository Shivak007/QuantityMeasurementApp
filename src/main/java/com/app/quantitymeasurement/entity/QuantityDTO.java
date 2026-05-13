package com.app.quantitymeasurement.entity;

import com.app.quantitymeasurement.unit.LengthUnit;
import com.app.quantitymeasurement.unit.WeightUnit;
import com.app.quantitymeasurement.unit.VolumeUnit;
import com.app.quantitymeasurement.unit.TemperatureUnit;

/**
 * QuantityDTO - Data Transfer Object for carrying quantity operation
 * requests between layers. Units made public for inter-package visibility.
 */
public class QuantityDTO {

    private final double firstValue;
    private final String firstUnit;
    private final double secondValue;
    private final String secondUnit;
    private final String operationType;
    private final String measurementType;

    public QuantityDTO(double firstValue, String firstUnit,
                       double secondValue, String secondUnit,
                       String operationType, String measurementType) {
        this.firstValue = firstValue;
        this.firstUnit = firstUnit;
        this.secondValue = secondValue;
        this.secondUnit = secondUnit;
        this.operationType = operationType;
        this.measurementType = measurementType;
    }

    public double getFirstValue()      { return firstValue; }
    public String getFirstUnit()       { return firstUnit; }
    public double getSecondValue()     { return secondValue; }
    public String getSecondUnit()      { return secondUnit; }
    public String getOperationType()   { return operationType; }
    public String getMeasurementType() { return measurementType; }

    // ---------- Unit resolution helpers ----------

    public static LengthUnit    toLengthUnit(String unit)      { return LengthUnit.valueOf(unit.toUpperCase()); }
    public static WeightUnit    toWeightUnit(String unit)      { return WeightUnit.valueOf(unit.toUpperCase()); }
    public static VolumeUnit    toVolumeUnit(String unit)      { return VolumeUnit.valueOf(unit.toUpperCase()); }
    public static TemperatureUnit toTemperatureUnit(String u)  { return TemperatureUnit.valueOf(u.toUpperCase()); }

    @Override
    public String toString() {
        return "QuantityDTO{" + operationType + ", " + measurementType +
               ", " + firstValue + " " + firstUnit +
               ", " + secondValue + " " + secondUnit + "}";
    }
}
