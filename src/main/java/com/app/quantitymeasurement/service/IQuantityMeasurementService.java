package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.List;

/**
 * IQuantityMeasurementService - Service layer contract for all
 * quantity measurement operations.
 */
public interface IQuantityMeasurementService {

    /**
     * Compares two quantities of the same measurement type.
     *
     * @return true if the quantities are equal (within tolerance)
     */
    boolean compare(QuantityDTO dto);

    /**
     * Adds two quantities and returns the result in the base unit.
     */
    double add(QuantityDTO dto);

    /**
     * Converts a single quantity to its equivalent in the base unit.
     */
    double convert(QuantityDTO dto);

    /**
     * Returns all measurement operations stored in the repository.
     */
    List<QuantityMeasurementEntity> getAllMeasurements();

    /**
     * Returns measurements filtered by operation type.
     */
    List<QuantityMeasurementEntity> getMeasurementsByOperation(String operationType);

    /**
     * Returns measurements filtered by measurement category.
     */
    List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType);

    /**
     * Returns the total count of stored measurements.
     */
    int getTotalCount();

    /**
     * Deletes all measurements from the repository.
     */
    void deleteAllMeasurements();

    /**
     * Returns pool or cache statistics from the underlying repository.
     */
    String getRepositoryStatistics();
}
