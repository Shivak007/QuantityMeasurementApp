package com.bridgelabz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest {
    // FEET TEST CASES

    @Test
    void testFeetEquality_SameValue() {
        assertTrue(QuantityMeasurementApp.checkFeetEquality(1.0, 1.0));
    }

    @Test
    void testFeetEquality_DifferentValue() {
        assertFalse(QuantityMeasurementApp.checkFeetEquality(1.0, 2.0));
    }

    @Test
    void testFeetEquality_SameReference() {
        QuantityMeasurementApp.Feet feet =
                new QuantityMeasurementApp.Feet(1.0);
        assertTrue(feet.equals(feet));
    }

    @Test
    void testFeetEquality_NullComparison() {
        QuantityMeasurementApp.Feet feet =
                new QuantityMeasurementApp.Feet(1.0);
        assertFalse(feet.equals(null));
    }

    @Test
    void testFeetEquality_NonNumericInput() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityMeasurementApp.Feet(Double.NaN));
    }
    // INCH TEST CASES
    @Test
    void testInchEquality_SameValue() {
        assertTrue(QuantityMeasurementApp.checkInchEquality(1.0, 1.0));
    }

    @Test
    void testInchEquality_DifferentValue() {
        assertFalse(QuantityMeasurementApp.checkInchEquality(1.0, 2.0));
    }

    @Test
    void testInchEquality_SameReference() {
        QuantityMeasurementApp.Inches inch =
                new QuantityMeasurementApp.Inches(1.0);
        assertTrue(inch.equals(inch));
    }

    @Test
    void testInchEquality_NullComparison() {
        QuantityMeasurementApp.Inches inch =
                new QuantityMeasurementApp.Inches(1.0);
        assertFalse(inch.equals(null));
    }

    @Test
    void testInchEquality_NonNumericInput() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityMeasurementApp.Inches(Double.POSITIVE_INFINITY));
    }
}