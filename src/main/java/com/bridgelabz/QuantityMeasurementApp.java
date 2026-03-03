package com.bridgelabz;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        QuantityLength a =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength b =
                new QuantityLength(12.0, LengthUnit.INCHES);

        System.out.println(a.convertTo(LengthUnit.INCHES));

        System.out.println(
                QuantityLength.add(a, b, LengthUnit.FEET));

        System.out.println(
                new QuantityLength(36.0, LengthUnit.INCHES)
                        .equals(new QuantityLength(1.0, LengthUnit.YARDS)));

        System.out.println(
                LengthUnit.INCHES.convertToBaseUnit(12.0));
    }
}