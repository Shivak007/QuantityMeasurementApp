package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.model.*;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.unit.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * QuantityMeasurementServiceImpl - UC17 Spring service.
 *
 * UC1-UC16 business logic (comparison, conversion, arithmetic) is preserved exactly.
 * Only the architecture is modernised: @Service, JPA repository injection, DTO returns.
 *
 * Persistence contract: BOTH successful and failed operations are saved.
 * On exception: persists error record, then rethrows.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final double TOLERANCE = 0.001;

    private final QuantityMeasurementRepository repository;

    // ---- COMPARE ----

    @Override
    public QuantityMeasurementDTO compare(QuantityInputDTO input) {
        QuantityDTO thisQ = input.getThisQuantityDTO();
        QuantityDTO thatQ = input.getThatQuantityDTO();

        log.debug("Service.compare: {} {} vs {} {}", thisQ.getValue(), thisQ.getUnit(),
                thatQ.getValue(), thatQ.getUnit());

        validateSameMeasurementType(thisQ, thatQ);

        QuantityMeasurementEntity entity = buildEntity(thisQ, thatQ, OperationType.COMPARE.name());
        try {
            double baseThis = toBaseValue(thisQ.getValue(), thisQ.getUnit(), thisQ.getMeasurementType());
            double baseThat = toBaseValue(thatQ.getValue(), thatQ.getUnit(), thatQ.getMeasurementType());
            boolean equal = Math.abs(baseThis - baseThat) < TOLERANCE;

            entity.setResultValue(equal ? 1.0 : 0.0);
            entity.setResultString(String.valueOf(equal));
            entity.setResultMeasurementType(thisQ.getMeasurementType().toUpperCase());
            entity.setError(false);

            log.info("Service.compare: result={}", equal);
            return QuantityMeasurementDTO.fromEntity(repository.save(entity));

        } catch (QuantityMeasurementException | IllegalArgumentException e) {
            return persistAndRethrow(entity, e);
        }
    }

    // ---- CONVERT ----

    @Override
    public QuantityMeasurementDTO convert(QuantityInputDTO input) {
        QuantityDTO thisQ = input.getThisQuantityDTO();
        QuantityDTO thatQ = input.getThatQuantityDTO();

        log.debug("Service.convert: {} {} -> base", thisQ.getValue(), thisQ.getUnit());

        QuantityMeasurementEntity entity = buildEntity(thisQ, thatQ, OperationType.CONVERT.name());
        try {
            double result = toBaseValue(thisQ.getValue(), thisQ.getUnit(), thisQ.getMeasurementType());
            String baseUnit = resolveBaseUnit(thisQ.getUnit(), thisQ.getMeasurementType());

            entity.setResultValue(result);
            entity.setResultUnit(baseUnit);
            entity.setResultString(result + " " + baseUnit);
            entity.setResultMeasurementType(thisQ.getMeasurementType().toUpperCase());
            entity.setError(false);

            log.info("Service.convert: {} {} = {} {}", thisQ.getValue(), thisQ.getUnit(), result, baseUnit);
            return QuantityMeasurementDTO.fromEntity(repository.save(entity));

        } catch (QuantityMeasurementException | IllegalArgumentException e) {
            return persistAndRethrow(entity, e);
        }
    }

    // ---- ADD ----

    @Override
    public QuantityMeasurementDTO add(QuantityInputDTO input) {
        QuantityDTO thisQ = input.getThisQuantityDTO();
        QuantityDTO thatQ = input.getThatQuantityDTO();

        log.debug("Service.add: {} {} + {} {}", thisQ.getValue(), thisQ.getUnit(),
                thatQ.getValue(), thatQ.getUnit());

        validateSameMeasurementType(thisQ, thatQ);

        QuantityMeasurementEntity entity = buildEntity(thisQ, thatQ, OperationType.ADD.name());
        try {
            double baseThis = toBaseValue(thisQ.getValue(), thisQ.getUnit(), thisQ.getMeasurementType());
            double baseThat = toBaseValue(thatQ.getValue(), thatQ.getUnit(), thatQ.getMeasurementType());
            double result = baseThis + baseThat;
            String baseUnit = resolveBaseUnit(thisQ.getUnit(), thisQ.getMeasurementType());

            entity.setResultValue(result);
            entity.setResultUnit(baseUnit);
            entity.setResultString(result + " " + baseUnit);
            entity.setResultMeasurementType(thisQ.getMeasurementType().toUpperCase());
            entity.setError(false);

            log.info("Service.add: result={} {}", result, baseUnit);
            return QuantityMeasurementDTO.fromEntity(repository.save(entity));

        } catch (QuantityMeasurementException | IllegalArgumentException e) {
            return persistAndRethrow(entity, e);
        }
    }

    // ---- SUBTRACT ----

    @Override
    public QuantityMeasurementDTO subtract(QuantityInputDTO input) {
        QuantityDTO thisQ = input.getThisQuantityDTO();
        QuantityDTO thatQ = input.getThatQuantityDTO();

        log.debug("Service.subtract: {} {} - {} {}", thisQ.getValue(), thisQ.getUnit(),
                thatQ.getValue(), thatQ.getUnit());

        validateSameMeasurementType(thisQ, thatQ);

        QuantityMeasurementEntity entity = buildEntity(thisQ, thatQ, OperationType.SUBTRACT.name());
        try {
            double baseThis = toBaseValue(thisQ.getValue(), thisQ.getUnit(), thisQ.getMeasurementType());
            double baseThat = toBaseValue(thatQ.getValue(), thatQ.getUnit(), thatQ.getMeasurementType());
            double result = baseThis - baseThat;
            String baseUnit = resolveBaseUnit(thisQ.getUnit(), thisQ.getMeasurementType());

            entity.setResultValue(result);
            entity.setResultUnit(baseUnit);
            entity.setResultString(result + " " + baseUnit);
            entity.setResultMeasurementType(thisQ.getMeasurementType().toUpperCase());
            entity.setError(false);

            log.info("Service.subtract: result={} {}", result, baseUnit);
            return QuantityMeasurementDTO.fromEntity(repository.save(entity));

        } catch (QuantityMeasurementException | IllegalArgumentException e) {
            return persistAndRethrow(entity, e);
        }
    }

    // ---- MULTIPLY ----

    @Override
    public QuantityMeasurementDTO multiply(QuantityInputDTO input) {
        QuantityDTO thisQ = input.getThisQuantityDTO();
        QuantityDTO thatQ = input.getThatQuantityDTO();

        log.debug("Service.multiply: {} {} * {} {}", thisQ.getValue(), thisQ.getUnit(),
                thatQ.getValue(), thatQ.getUnit());

        validateSameMeasurementType(thisQ, thatQ);

        QuantityMeasurementEntity entity = buildEntity(thisQ, thatQ, OperationType.MULTIPLY.name());
        try {
            double baseThis = toBaseValue(thisQ.getValue(), thisQ.getUnit(), thisQ.getMeasurementType());
            double baseThat = toBaseValue(thatQ.getValue(), thatQ.getUnit(), thatQ.getMeasurementType());
            double result = baseThis * baseThat;
            String baseUnit = resolveBaseUnit(thisQ.getUnit(), thisQ.getMeasurementType());

            entity.setResultValue(result);
            entity.setResultUnit(baseUnit);
            entity.setResultString(result + " " + baseUnit + "^2");
            entity.setResultMeasurementType(thisQ.getMeasurementType().toUpperCase());
            entity.setError(false);

            log.info("Service.multiply: result={}", result);
            return QuantityMeasurementDTO.fromEntity(repository.save(entity));

        } catch (QuantityMeasurementException | IllegalArgumentException e) {
            return persistAndRethrow(entity, e);
        }
    }

    // ---- DIVIDE ----

    @Override
    public QuantityMeasurementDTO divide(QuantityInputDTO input) {
        QuantityDTO thisQ = input.getThisQuantityDTO();
        QuantityDTO thatQ = input.getThatQuantityDTO();

        log.debug("Service.divide: {} {} / {} {}", thisQ.getValue(), thisQ.getUnit(),
                thatQ.getValue(), thatQ.getUnit());

        validateSameMeasurementType(thisQ, thatQ);

        QuantityMeasurementEntity entity = buildEntity(thisQ, thatQ, OperationType.DIVIDE.name());
        try {
            double baseThis = toBaseValue(thisQ.getValue(), thisQ.getUnit(), thisQ.getMeasurementType());
            double baseThat = toBaseValue(thatQ.getValue(), thatQ.getUnit(), thatQ.getMeasurementType());

            if (Math.abs(baseThat) < TOLERANCE) {
                throw new ArithmeticException("Division by zero: thatQuantity resolves to zero in base unit");
            }

            double result = baseThis / baseThat;
            String baseUnit = resolveBaseUnit(thisQ.getUnit(), thisQ.getMeasurementType());

            entity.setResultValue(result);
            entity.setResultUnit(baseUnit);
            entity.setResultString(String.valueOf(result));
            entity.setResultMeasurementType(thisQ.getMeasurementType().toUpperCase());
            entity.setError(false);

            log.info("Service.divide: result={}", result);
            return QuantityMeasurementDTO.fromEntity(repository.save(entity));

        } catch (ArithmeticException e) {
            entity.setError(true);
            entity.setErrorMessage(e.getMessage());
            repository.save(entity);
            throw e;
        } catch (QuantityMeasurementException | IllegalArgumentException e) {
            return persistAndRethrow(entity, e);
        }
    }

    // ---- HISTORY / QUERY ----

    @Override
    public List<QuantityMeasurementDTO> getHistoryByOperation(String operation) {
        return QuantityMeasurementDTO.fromEntities(
                repository.findByOperation(operation.toUpperCase()));
    }

    @Override
    public List<QuantityMeasurementDTO> getHistoryByType(String measurementType) {
        return QuantityMeasurementDTO.fromEntities(
                repository.findByThisMeasurementType(measurementType.toUpperCase()));
    }

    @Override
    public List<QuantityMeasurementDTO> getErrorHistory() {
        return QuantityMeasurementDTO.fromEntities(repository.findByIsErrorTrue());
    }

    @Override
    public long countByOperation(String operation) {
        return repository.countByOperationAndErrorFalse(operation.toUpperCase());
    }

    // ---- Private helpers (UC16 business logic preserved) ----

    /**
     * Converts value+unit to the base unit value.
     * Temperature uses special formula; all others use multiplication factor.
     * UC1-UC14 conversion logic preserved exactly.
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

    private String resolveBaseUnit(String unitStr, String measurementType) {
        switch (measurementType.toUpperCase()) {
            case "LENGTH":      return LengthUnit.valueOf(unitStr.toUpperCase()).getBaseUnit();
            case "WEIGHT":      return WeightUnit.valueOf(unitStr.toUpperCase()).getBaseUnit();
            case "VOLUME":      return VolumeUnit.valueOf(unitStr.toUpperCase()).getBaseUnit();
            case "TEMPERATURE": return TemperatureUnit.valueOf(unitStr.toUpperCase()).getBaseUnit();
            default:            return "UNKNOWN";
        }
    }

    private void validateSameMeasurementType(QuantityDTO thisQ, QuantityDTO thatQ) {
        if (!thisQ.getMeasurementType().equalsIgnoreCase(thatQ.getMeasurementType())) {
            throw new QuantityMeasurementException(
                "Cannot operate on different measurement types: "
                + thisQ.getMeasurementType() + " vs " + thatQ.getMeasurementType());
        }
    }

    private QuantityMeasurementEntity buildEntity(QuantityDTO thisQ, QuantityDTO thatQ, String operation) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(thisQ.getValue());
        entity.setThisUnit(thisQ.getUnit().toUpperCase());
        entity.setThisMeasurementType(thisQ.getMeasurementType().toUpperCase());
        entity.setThatValue(thatQ.getValue());
        entity.setThatUnit(thatQ.getUnit().toUpperCase());
        entity.setThatMeasurementType(thatQ.getMeasurementType().toUpperCase());
        entity.setOperation(operation);
        entity.setError(false);
        return entity;
    }

    private QuantityMeasurementDTO persistAndRethrow(QuantityMeasurementEntity entity, RuntimeException e) {
        entity.setError(true);
        entity.setErrorMessage(e.getMessage());
        repository.save(entity);
        throw e;
    }
}
