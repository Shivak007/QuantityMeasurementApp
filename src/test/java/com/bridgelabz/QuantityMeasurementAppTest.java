package com.bridgelabz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    @Test
    void testEquality_YardToYard_SameValue() {
        var q1 = new QuantityMeasurementApp.QuantityLength(
                1.0,
                QuantityMeasurementApp.LengthUnit.YARDS);

        var q2 = new QuantityMeasurementApp.QuantityLength(
                1.0,
                QuantityMeasurementApp.LengthUnit.YARDS);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_YardToFeet_EquivalentValue() {
        var yard = new QuantityMeasurementApp.QuantityLength(
                1.0,
                QuantityMeasurementApp.LengthUnit.YARDS);

        var feet = new QuantityMeasurementApp.QuantityLength(
                3.0,
                QuantityMeasurementApp.LengthUnit.FEET);

        assertTrue(yard.equals(feet));
        assertTrue(feet.equals(yard));
    }

    @Test
    void testEquality_YardToInches_EquivalentValue() {
        var yard = new QuantityMeasurementApp.QuantityLength(
                1.0,
                QuantityMeasurementApp.LengthUnit.YARDS);

        var inches = new QuantityMeasurementApp.QuantityLength(
                36.0,
                QuantityMeasurementApp.LengthUnit.INCH);

        assertTrue(yard.equals(inches));
    }

    @Test
    void testEquality_CentimeterToInches_EquivalentValue() {
        var cm = new QuantityMeasurementApp.QuantityLength(
                1.0,
                QuantityMeasurementApp.LengthUnit.CENTIMETERS);

        var inch = new QuantityMeasurementApp.QuantityLength(
                0.393701,
                QuantityMeasurementApp.LengthUnit.INCH);

        assertTrue(cm.equals(inch));
    }

    @Test
    void testEquality_MultiUnit_TransitiveProperty() {
        var yard = new QuantityMeasurementApp.QuantityLength(
                1.0,
                QuantityMeasurementApp.LengthUnit.YARDS);

        var feet = new QuantityMeasurementApp.QuantityLength(
                3.0,
                QuantityMeasurementApp.LengthUnit.FEET);

        var inches = new QuantityMeasurementApp.QuantityLength(
                36.0,
                QuantityMeasurementApp.LengthUnit.INCH);

        assertTrue(yard.equals(feet));
        assertTrue(feet.equals(inches));
        assertTrue(yard.equals(inches));
    }

    @Test
    void testEquality_InvalidUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityMeasurementApp.QuantityLength(
                        1.0,
                        null));
    }

    @Test
    void testEquality_SameReference() {
        var q = new QuantityMeasurementApp.QuantityLength(
                2.0,
                QuantityMeasurementApp.LengthUnit.YARDS);

        assertTrue(q.equals(q));
    }

    @Test
    void testEquality_NullComparison() {
        var q = new QuantityMeasurementApp.QuantityLength(
                2.0,
                QuantityMeasurementApp.LengthUnit.YARDS);

        assertFalse(q.equals(null));
    }
}