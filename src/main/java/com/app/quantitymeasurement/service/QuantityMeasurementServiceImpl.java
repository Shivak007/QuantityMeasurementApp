package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.unit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * QuantityMeasurementServiceImpl - Business logic for comparing, adding
 * and converting quantities. Delegates persistence to the injected repository.
 *
 * UC16: Repository injected via DI; supports both cache and database repos.
 */
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);
    private static final double TOLERANCE = 0.001;

    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        this.repository = repository;
        logger.info("QuantityMeasurementServiceImpl: Initialised with repository: {}",
            repository.getClass().getSimpleName());
    }

    // ---- COMPARE ----

    @Override
    public boolean compare(QuantityDTO dto) {
        logger.debug("Service.compare: {} {} vs {} {}",
            dto.getFirstValue(), dto.getFirstUnit(), dto.getSecondValue(), dto.getSecondUnit());

        double baseFirst  = toBaseValue(dto.getFirstValue(),  dto.getFirstUnit(),  dto.getMeasurementType());
        double baseSecond = toBaseValue(dto.getSecondValue(), dto.getSecondUnit(), dto.getMeasurementType());
        boolean equal     = Math.abs(baseFirst - baseSecond) < TOLERANCE;

        persist(dto, 0.0, equal);
        logger.info("Service.compare: {} {} == {} {}? {}",
            dto.getFirstValue(), dto.getFirstUnit(),
            dto.getSecondValue(), dto.getSecondUnit(), equal);
        return equal;
    }

    // ---- ADD ----

    @Override
    public double add(QuantityDTO dto) {
        logger.debug("Service.add: {} {} + {} {}",
            dto.getFirstValue(), dto.getFirstUnit(), dto.getSecondValue(), dto.getSecondUnit());

        double baseFirst  = toBaseValue(dto.getFirstValue(),  dto.getFirstUnit(),  dto.getMeasurementType());
        double baseSecond = toBaseValue(dto.getSecondValue(), dto.getSecondUnit(), dto.getMeasurementType());
        double result     = baseFirst + baseSecond;

        persist(dto, result, false);
        logger.info("Service.add: {} {} + {} {} = {} (base unit)",
            dto.getFirstValue(), dto.getFirstUnit(),
            dto.getSecondValue(), dto.getSecondUnit(), result);
        return result;
    }

    // ---- CONVERT ----

    @Override
    public double convert(QuantityDTO dto) {
        logger.debug("Service.convert: {} {} -> base", dto.getFirstValue(), dto.getFirstUnit());
        double result = toBaseValue(dto.getFirstValue(), dto.getFirstUnit(), dto.getMeasurementType());
        persist(dto, result, false);
        logger.info("Service.convert: {} {} = {} (base unit)",
            dto.getFirstValue(), dto.getFirstUnit(), result);
        return result;
    }

    // ---- Repository delegation ----

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return repository.getAllMeasurements();
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operationType) {
        return repository.getMeasurementsByOperation(operationType);
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType) {
        return repository.getMeasurementsByType(measurementType);
    }

    @Override
    public int getTotalCount() {
        return repository.getTotalCount();
    }

    @Override
    public void deleteAllMeasurements() {
        repository.deleteAll();
        logger.info("Service: All measurements deleted.");
    }

    @Override
    public String getRepositoryStatistics() {
        return repository.getPoolStatistics();
    }

    // ---- Private helpers ----

    /**
     * Converts a value+unit to the base unit value, dispatching on measurementType.
     */
    private double toBaseValue(double value, String unitStr, String measurementType) {
        switch (measurementType.toUpperCase()) {
            case "LENGTH":
                return value * LengthUnit.valueOf(unitStr.toUpperCase()).getConversionFactor();
            case "WEIGHT":
                return value * WeightUnit.valueOf(unitStr.toUpperCase()).getConversionFactor();
            case "VOLUME":
                return value * VolumeUnit.valueOf(unitStr.toUpperCase()).getConversionFactor();
            case "TEMPERATURE":
                return TemperatureUnit.valueOf(unitStr.toUpperCase()).toCelsius(value);
            default:
                throw new QuantityMeasurementException("Unknown measurement type: " + measurementType);
        }
    }

    /** Builds and persists a QuantityMeasurementEntity from a DTO and operation result. */
    private void persist(QuantityDTO dto, double result, boolean resultBoolean) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
            dto.getOperationType(),
            dto.getMeasurementType(),
            dto.getFirstValue(),
            dto.getFirstUnit(),
            dto.getSecondValue(),
            dto.getSecondUnit(),
            result,
            resultBoolean
        );
        repository.save(entity);
    }
}
