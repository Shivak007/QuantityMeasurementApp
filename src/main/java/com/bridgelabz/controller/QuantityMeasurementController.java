package com.bridgelabz.controller;

import com.bridgelabz.model.QuantityDTO;
import com.bridgelabz.service.IQuantityMeasurementService;

public class QuantityMeasurementController {

    private IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        this.service = service;
    }

    public void performAddition(QuantityDTO q1, QuantityDTO q2) {
        QuantityDTO result = service.add(q1, q2);
        System.out.println("Addition Result: " + result.getValue() + " " + result.getUnit());
    }

    public void performComparison(QuantityDTO q1, QuantityDTO q2) {
        boolean result = service.compare(q1, q2);
        System.out.println("Comparison Result: " + result);
    }

    public void performConversion(QuantityDTO q, String targetUnit) {
        QuantityDTO result = service.convert(q, targetUnit);
        System.out.println("Conversion Result: " + result.getValue() + " " + result.getUnit());
    }
}