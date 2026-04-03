package com.bridgelabz.service;

import com.bridgelabz.model.QuantityDTO;

public interface IQuantityMeasurementService {

    QuantityDTO add(QuantityDTO q1, QuantityDTO q2);

    QuantityDTO convert(QuantityDTO q, String targetUnit);

    boolean compare(QuantityDTO q1, QuantityDTO q2);
}