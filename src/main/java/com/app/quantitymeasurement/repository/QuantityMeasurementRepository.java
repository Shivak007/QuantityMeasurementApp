package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * QuantityMeasurementRepository - JPA repository replacing UC16 JDBC repositories.
 * Replaces: IQuantityMeasurementRepository, QuantityMeasurementDatabaseRepository,
 *           QuantityMeasurementCacheRepository.
 */
@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {

    List<QuantityMeasurementEntity> findByOperation(String operation);

    List<QuantityMeasurementEntity> findByThisMeasurementType(String measurementType);

    @Query("SELECT e FROM QuantityMeasurementEntity e WHERE e.error = true")
    List<QuantityMeasurementEntity> findByIsErrorTrue();

    @Query("SELECT COUNT(e) FROM QuantityMeasurementEntity e WHERE e.operation = :operation AND e.error = false")
    long countByOperationAndErrorFalse(@Param("operation") String operation);
}
