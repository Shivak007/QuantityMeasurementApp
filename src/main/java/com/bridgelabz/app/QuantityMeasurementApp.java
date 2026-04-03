package com.bridgelabz.app;

import com.bridgelabz.controller.QuantityMeasurementController;
import com.bridgelabz.model.QuantityDTO;
import com.bridgelabz.repository.QuantityMeasurementCacheRepository;
import com.bridgelabz.service.QuantityMeasurementServiceImpl;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        QuantityMeasurementCacheRepository repo = QuantityMeasurementCacheRepository.getInstance();

        QuantityMeasurementServiceImpl service = new QuantityMeasurementServiceImpl(repo);

        QuantityMeasurementController controller = new QuantityMeasurementController(service);

        QuantityDTO q1 = new QuantityDTO(10, "FEET");
        QuantityDTO q2 = new QuantityDTO(120, "INCH");

        controller.performComparison(q1, q2);
        controller.performAddition(q1, q2);
        controller.performConversion(q1, "INCH");
    }
}