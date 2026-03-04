package com.bridgelabz;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        Quantity<LengthUnit> length1 =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> length2 =
                new Quantity<>(12.0, LengthUnit.INCHES);

        System.out.println(length1.equals(length2));

        System.out.println(
                length1.convertTo(LengthUnit.INCHES));

        System.out.println(
                length1.add(length2, LengthUnit.FEET));



        Quantity<WeightUnit> weight1 =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> weight2 =
                new Quantity<>(1000.0, WeightUnit.GRAM);

        System.out.println(weight1.equals(weight2));

        System.out.println(
                weight1.convertTo(WeightUnit.GRAM));

        System.out.println(
                weight1.add(weight2, WeightUnit.KILOGRAM));
    }
}