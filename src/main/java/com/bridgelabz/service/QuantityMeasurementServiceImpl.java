package com.bridgelabz.service;

import com.bridgelabz.Quantity;
import com.bridgelabz.UnitFactory;
import com.bridgelabz.IMeasurable;
import com.bridgelabz.model.*;
import com.bridgelabz.repository.IQuantityMeasurementRepository;
import com.bridgelabz.exception.QuantityMeasurementException;

@SuppressWarnings("unchecked")
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    @Override
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2) {
        try {

            IMeasurable unit1 = UnitFactory.getUnit(q1.getUnit());
            IMeasurable unit2 = UnitFactory.getUnit(q2.getUnit());

            Quantity quantity1 = new Quantity(q1.getValue(), unit1);
            Quantity quantity2 = new Quantity(q2.getValue(), unit2);

            Quantity result = quantity1.add(quantity2);

            QuantityDTO resDTO =
                    new QuantityDTO(result.getValue(), result.getUnit().toString());

            repository.save(new QuantityMeasurementEntity(q1, q2, resDTO, "ADD"));

            return resDTO;

        } catch (Exception e) {
            throw new QuantityMeasurementException(e.getMessage());
        }
    }

    @Override
    public QuantityDTO convert(QuantityDTO q, String targetUnit) {
        try {

            IMeasurable unit = UnitFactory.getUnit(q.getUnit());
            IMeasurable target = UnitFactory.getUnit(targetUnit);

            Quantity quantity = new Quantity(q.getValue(), unit);
            Quantity result = quantity.convertTo(target);

            return new QuantityDTO(result.getValue(), result.getUnit().toString());

        } catch (Exception e) {
            throw new QuantityMeasurementException(e.getMessage());
        }
    }

    @Override
    public boolean compare(QuantityDTO q1, QuantityDTO q2) {
        try {

            IMeasurable unit1 = UnitFactory.getUnit(q1.getUnit());
            IMeasurable unit2 = UnitFactory.getUnit(q2.getUnit());

            Quantity quantity1 = new Quantity(q1.getValue(), unit1);
            Quantity quantity2 = new Quantity(q2.getValue(), unit2);

            return quantity1.equals(quantity2);

        } catch (Exception e) {
            throw new QuantityMeasurementException(e.getMessage());
        }
    }
}