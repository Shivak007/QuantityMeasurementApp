package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.model.QuantityInputDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;

import java.util.List;

/**
 * IQuantityMeasurementService - Service contract for UC17 Spring Boot migration.
 * All operations return QuantityMeasurementDTO for full REST response support.
 */
public interface IQuantityMeasurementService {

    QuantityMeasurementDTO compare(QuantityInputDTO input);

    QuantityMeasurementDTO convert(QuantityInputDTO input);

    QuantityMeasurementDTO add(QuantityInputDTO input);

    QuantityMeasurementDTO subtract(QuantityInputDTO input);

    QuantityMeasurementDTO multiply(QuantityInputDTO input);

    QuantityMeasurementDTO divide(QuantityInputDTO input);

    List<QuantityMeasurementDTO> getHistoryByOperation(String operation);

    List<QuantityMeasurementDTO> getHistoryByType(String measurementType);

    List<QuantityMeasurementDTO> getErrorHistory();

    long countByOperation(String operation);
}
