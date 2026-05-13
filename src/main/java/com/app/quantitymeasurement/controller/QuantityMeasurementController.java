package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * QuantityMeasurementController - Thin controller that bridges client
 * requests to the service layer. Contains no business logic.
 *
 * UC16: Updated imports for new package structure + SLF4J logging.
 */
public class QuantityMeasurementController {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementController.class);

    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        this.service = service;
        logger.info("QuantityMeasurementController: Initialised.");
    }

    // ---- Operations ----

    public boolean compare(double firstValue, String firstUnit,
                           double secondValue, String secondUnit,
                           String measurementType) {
        logger.debug("Controller.compare: {} {} vs {} {} [{}]",
            firstValue, firstUnit, secondValue, secondUnit, measurementType);
        QuantityDTO dto = new QuantityDTO(firstValue, firstUnit,
                                          secondValue, secondUnit,
                                          "COMPARE", measurementType);
        return service.compare(dto);
    }

    public double add(double firstValue, String firstUnit,
                      double secondValue, String secondUnit,
                      String measurementType) {
        logger.debug("Controller.add: {} {} + {} {} [{}]",
            firstValue, firstUnit, secondValue, secondUnit, measurementType);
        QuantityDTO dto = new QuantityDTO(firstValue, firstUnit,
                                          secondValue, secondUnit,
                                          "ADD", measurementType);
        return service.add(dto);
    }

    public double convert(double value, String fromUnit, String measurementType) {
        logger.debug("Controller.convert: {} {} -> base [{}]", value, fromUnit, measurementType);
        QuantityDTO dto = new QuantityDTO(value, fromUnit,
                                          0.0, fromUnit,
                                          "CONVERT", measurementType);
        return service.convert(dto);
    }

    // ---- Query methods (UC16) ----

    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return service.getAllMeasurements();
    }

    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operationType) {
        return service.getMeasurementsByOperation(operationType);
    }

    public List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType) {
        return service.getMeasurementsByType(measurementType);
    }

    public int getTotalCount() {
        return service.getTotalCount();
    }

    public void deleteAllMeasurements() {
        service.deleteAllMeasurements();
    }

    public String getRepositoryStatistics() {
        return service.getRepositoryStatistics();
    }
}
