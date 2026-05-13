package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.List;
import java.util.Map;

/**
 * IQuantityMeasurementRepository - Repository interface for persisting
 * and retrieving QuantityMeasurementEntity records.
 *
 * UC16: Extended with database-specific query and lifecycle methods.
 */
public interface IQuantityMeasurementRepository {

    /**
     * Persists a QuantityMeasurementEntity. Returns the entity with its
     * generated ID set (if applicable).
     */
    QuantityMeasurementEntity save(QuantityMeasurementEntity entity);

    /**
     * Retrieves all persisted measurement entities.
     */
    List<QuantityMeasurementEntity> getAllMeasurements();

    /**
     * Retrieves all measurements filtered by operation type
     * (e.g., "COMPARE", "ADD", "CONVERT").
     */
    List<QuantityMeasurementEntity> getMeasurementsByOperation(String operationType);

    /**
     * Retrieves all measurements filtered by measurement type
     * (e.g., "LENGTH", "WEIGHT", "VOLUME", "TEMPERATURE").
     */
    List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType);

    /**
     * Returns the total count of stored measurements.
     */
    int getTotalCount();

    /**
     * Deletes all stored measurements (useful for testing / reset).
     */
    void deleteAll();

    // ---- Default lifecycle / monitoring methods (UC16) ----

    /**
     * Returns pool or cache statistics as a descriptive string.
     * Implementations that do not use a pool may return "N/A".
     */
    default String getPoolStatistics() {
        return "N/A - no connection pool used by this repository.";
    }

    /**
     * Releases any resources held by the repository (connections, caches, etc.).
     * Called on application shutdown.
     */
    default void releaseResources() {
        // default no-op
    }
}
