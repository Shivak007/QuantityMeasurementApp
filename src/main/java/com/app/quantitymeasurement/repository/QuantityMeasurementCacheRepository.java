package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * QuantityMeasurementCacheRepository - In-memory implementation of
 * IQuantityMeasurementRepository using a thread-safe list.
 *
 * Implements all new UC16 interface methods with sensible in-memory behaviour.
 */
public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementCacheRepository.class);

    private final List<QuantityMeasurementEntity> cache = new CopyOnWriteArrayList<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    public QuantityMeasurementCacheRepository() {
        logger.info("QuantityMeasurementCacheRepository: In-memory cache repository initialised.");
    }

    @Override
    public QuantityMeasurementEntity save(QuantityMeasurementEntity entity) {
        entity.setId(idSequence.getAndIncrement());
        cache.add(entity);
        logger.debug("CacheRepository: Saved entity id={} op={} type={}",
            entity.getId(), entity.getOperationType(), entity.getMeasurementType());
        return entity;
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        logger.debug("CacheRepository: Retrieving all {} measurements.", cache.size());
        return new ArrayList<>(cache);
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operationType) {
        List<QuantityMeasurementEntity> result = cache.stream()
            .filter(e -> operationType.equalsIgnoreCase(e.getOperationType()))
            .collect(Collectors.toList());
        logger.debug("CacheRepository: Found {} entities for operation '{}'.", result.size(), operationType);
        return result;
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType) {
        List<QuantityMeasurementEntity> result = cache.stream()
            .filter(e -> measurementType.equalsIgnoreCase(e.getMeasurementType()))
            .collect(Collectors.toList());
        logger.debug("CacheRepository: Found {} entities for type '{}'.", result.size(), measurementType);
        return result;
    }

    @Override
    public int getTotalCount() {
        return cache.size();
    }

    @Override
    public void deleteAll() {
        cache.clear();
        idSequence.set(1);
        logger.info("CacheRepository: All measurements deleted.");
    }

    @Override
    public String getPoolStatistics() {
        return "CacheRepository[size=" + cache.size() + ", nextId=" + idSequence.get() + "]";
    }

    @Override
    public void releaseResources() {
        deleteAll();
        logger.info("CacheRepository: Resources released.");
    }
}
