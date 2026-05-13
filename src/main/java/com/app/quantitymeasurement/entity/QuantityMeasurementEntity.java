package com.app.quantitymeasurement.entity;

import java.time.LocalDateTime;

/**
 * QuantityMeasurementEntity - POJO representing a persisted quantity
 * measurement operation. Maps to the quantity_measurement_entity table.
 */
public class QuantityMeasurementEntity {

    private long          id;
    private String        operationType;
    private String        measurementType;
    private double        firstValue;
    private String        firstUnit;
    private double        secondValue;
    private String        secondUnit;
    private double        result;
    private boolean       resultBoolean;
    private LocalDateTime createdAt;

    // ---- No-arg constructor ----
    public QuantityMeasurementEntity() {
        this.createdAt = LocalDateTime.now();
    }

    // ---- Full constructor ----
    public QuantityMeasurementEntity(String operationType, String measurementType,
                                     double firstValue, String firstUnit,
                                     double secondValue, String secondUnit,
                                     double result, boolean resultBoolean) {
        this.operationType   = operationType;
        this.measurementType = measurementType;
        this.firstValue      = firstValue;
        this.firstUnit       = firstUnit;
        this.secondValue     = secondValue;
        this.secondUnit      = secondUnit;
        this.result          = result;
        this.resultBoolean   = resultBoolean;
        this.createdAt       = LocalDateTime.now();
    }

    // ---- Getters and Setters ----
    public long          getId()             { return id; }
    public void          setId(long id)      { this.id = id; }

    public String        getOperationType()                         { return operationType; }
    public void          setOperationType(String operationType)     { this.operationType = operationType; }

    public String        getMeasurementType()                       { return measurementType; }
    public void          setMeasurementType(String measurementType) { this.measurementType = measurementType; }

    public double        getFirstValue()                  { return firstValue; }
    public void          setFirstValue(double firstValue) { this.firstValue = firstValue; }

    public String        getFirstUnit()                   { return firstUnit; }
    public void          setFirstUnit(String firstUnit)   { this.firstUnit = firstUnit; }

    public double        getSecondValue()                   { return secondValue; }
    public void          setSecondValue(double secondValue) { this.secondValue = secondValue; }

    public String        getSecondUnit()                    { return secondUnit; }
    public void          setSecondUnit(String secondUnit)   { this.secondUnit = secondUnit; }

    public double        getResult()               { return result; }
    public void          setResult(double result)  { this.result = result; }

    public boolean       isResultBoolean()                         { return resultBoolean; }
    public void          setResultBoolean(boolean resultBoolean)   { this.resultBoolean = resultBoolean; }

    public LocalDateTime getCreatedAt()                       { return createdAt; }
    public void          setCreatedAt(LocalDateTime createdAt){ this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "QuantityMeasurementEntity{" +
               "id=" + id +
               ", op='" + operationType + '\'' +
               ", type='" + measurementType + '\'' +
               ", " + firstValue + " " + firstUnit +
               " <-> " + secondValue + " " + secondUnit +
               ", result=" + result +
               ", resultBool=" + resultBoolean +
               ", at=" + createdAt +
               '}';
    }
}
