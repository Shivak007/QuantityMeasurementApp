package com.bridgelabz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    @Test
    void testEquality_FeetToFeet_SameValue() {
        var q1 = new QuantityMeasurementApp.QuantityLength(
                1.0,
                QuantityMeasurementApp.LengthUnit.FEET);

        var q2 = new QuantityMeasurementApp.QuantityLength(
                1.0,
                QuantityMeasurementApp.LengthUnit.FEET);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_InchToInch_SameValue() {
        var q1 = new QuantityMeasurementApp.QuantityLength(
                1.0,
                QuantityMeasurementApp.LengthUnit.INCH);

        var q2 = new QuantityMeasurementApp.QuantityLength(
                1.0,
                QuantityMeasurementApp.LengthUnit.INCH);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_FeetToInch_EquivalentValue() {
        var q1 = new QuantityMeasurementApp.QuantityLength(
                1.0,
                QuantityMeasurementApp.LengthUnit.FEET);

        var q2 = new QuantityMeasurementApp.QuantityLength(
                12.0,
                QuantityMeasurementApp.LengthUnit.INCH);

        assertTrue(q1.equals(q2));
        assertTrue(q2.equals(q1)); // symmetry
    }

    @Test
    void testEquality_DifferentValue() {
        var q1 = new QuantityMeasurementApp.QuantityLength(
                1.0,
                QuantityMeasurementApp.LengthUnit.FEET);

        var q2 = new QuantityMeasurementApp.QuantityLength(
                2.0,
                QuantityMeasurementApp.LengthUnit.FEET);

        assertFalse(q1.equals(q2));
    }

    @Test
    void testEquality_SameReference() {
        var q1 = new QuantityMeasurementApp.QuantityLength(
                1.0,
                QuantityMeasurementApp.LengthUnit.FEET);

        assertTrue(q1.equals(q1));
    }

    @Test
    void testEquality_NullComparison() {
        var q1 = new QuantityMeasurementApp.QuantityLength(
                1.0,
                QuantityMeasurementApp.LengthUnit.FEET);

        assertFalse(q1.equals(null));
    }

    @Test
    void testInvalidUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityMeasurementApp.QuantityLength(
                        1.0,
                        null));
    }

    @Test
    void testInvalidNumericValue() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityMeasurementApp.QuantityLength(
                        Double.NaN,
                        QuantityMeasurementApp.LengthUnit.FEET));
    }
}