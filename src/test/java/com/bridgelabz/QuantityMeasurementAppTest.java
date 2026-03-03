package com.bridgelabz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    private static final double EPSILON = 1e-6;

    // Explicit Target Unit – FEET
    @Test
    void testAddition_ExplicitTargetUnit_Feet() {

        var a = new QuantityMeasurementApp.QuantityLength(
                1.0, QuantityMeasurementApp.LengthUnit.FEET);

        var b = new QuantityMeasurementApp.QuantityLength(
                12.0, QuantityMeasurementApp.LengthUnit.INCHES);

        var result = QuantityMeasurementApp.QuantityLength.add(
                a, b, QuantityMeasurementApp.LengthUnit.FEET);

        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(QuantityMeasurementApp.LengthUnit.FEET, result.getUnit());
    }

    // Explicit Target Unit – INCHES
    @Test
    void testAddition_ExplicitTargetUnit_Inches() {

        var a = new QuantityMeasurementApp.QuantityLength(
                1.0, QuantityMeasurementApp.LengthUnit.FEET);

        var b = new QuantityMeasurementApp.QuantityLength(
                12.0, QuantityMeasurementApp.LengthUnit.INCHES);

        var result = QuantityMeasurementApp.QuantityLength.add(
                a, b, QuantityMeasurementApp.LengthUnit.INCHES);

        assertEquals(24.0, result.getValue(), EPSILON);
        assertEquals(QuantityMeasurementApp.LengthUnit.INCHES, result.getUnit());
    }

    // Explicit Target Unit – YARDS
    @Test
    void testAddition_ExplicitTargetUnit_Yards() {

        var a = new QuantityMeasurementApp.QuantityLength(
                1.0, QuantityMeasurementApp.LengthUnit.FEET);

        var b = new QuantityMeasurementApp.QuantityLength(
                12.0, QuantityMeasurementApp.LengthUnit.INCHES);

        var result = QuantityMeasurementApp.QuantityLength.add(
                a, b, QuantityMeasurementApp.LengthUnit.YARDS);

        assertEquals(0.6666667, result.getValue(), 1e-4);
        assertEquals(QuantityMeasurementApp.LengthUnit.YARDS, result.getUnit());
    }

    // Explicit Target Unit – CENTIMETERS
    @Test
    void testAddition_ExplicitTargetUnit_Centimeters() {

        var a = new QuantityMeasurementApp.QuantityLength(
                1.0, QuantityMeasurementApp.LengthUnit.INCHES);

        var b = new QuantityMeasurementApp.QuantityLength(
                1.0, QuantityMeasurementApp.LengthUnit.INCHES);

        var result = QuantityMeasurementApp.QuantityLength.add(
                a, b, QuantityMeasurementApp.LengthUnit.CENTIMETERS);

        assertEquals(5.08, result.getValue(), 1e-2);
        assertEquals(QuantityMeasurementApp.LengthUnit.CENTIMETERS, result.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Commutativity() {

        var a = new QuantityMeasurementApp.QuantityLength(
                1.0, QuantityMeasurementApp.LengthUnit.FEET);

        var b = new QuantityMeasurementApp.QuantityLength(
                12.0, QuantityMeasurementApp.LengthUnit.INCHES);

        var result1 = QuantityMeasurementApp.QuantityLength.add(
                a, b, QuantityMeasurementApp.LengthUnit.YARDS);

        var result2 = QuantityMeasurementApp.QuantityLength.add(
                b, a, QuantityMeasurementApp.LengthUnit.YARDS);

        assertEquals(result1.getValue(), result2.getValue(), EPSILON);
    }

    // Zero Operand
    @Test
    void testAddition_ExplicitTargetUnit_WithZero() {

        var a = new QuantityMeasurementApp.QuantityLength(
                5.0, QuantityMeasurementApp.LengthUnit.FEET);

        var b = new QuantityMeasurementApp.QuantityLength(
                0.0, QuantityMeasurementApp.LengthUnit.INCHES);

        var result = QuantityMeasurementApp.QuantityLength.add(
                a, b, QuantityMeasurementApp.LengthUnit.YARDS);

        assertEquals(1.6666667, result.getValue(), 1e-4);
    }

    // Negative Values
    @Test
    void testAddition_ExplicitTargetUnit_NegativeValues() {

        var a = new QuantityMeasurementApp.QuantityLength(
                5.0, QuantityMeasurementApp.LengthUnit.FEET);

        var b = new QuantityMeasurementApp.QuantityLength(
                -2.0, QuantityMeasurementApp.LengthUnit.FEET);

        var result = QuantityMeasurementApp.QuantityLength.add(
                a, b, QuantityMeasurementApp.LengthUnit.INCHES);

        assertEquals(36.0, result.getValue(), EPSILON);
    }

    // Large to Small Scale
    @Test
    void testAddition_ExplicitTargetUnit_LargeToSmallScale() {

        var a = new QuantityMeasurementApp.QuantityLength(
                1000.0, QuantityMeasurementApp.LengthUnit.FEET);

        var b = new QuantityMeasurementApp.QuantityLength(
                500.0, QuantityMeasurementApp.LengthUnit.FEET);

        var result = QuantityMeasurementApp.QuantityLength.add(
                a, b, QuantityMeasurementApp.LengthUnit.INCHES);

        assertEquals(18000.0, result.getValue(), EPSILON);
    }

    // Null Target Unit Validation
    @Test
    void testAddition_ExplicitTargetUnit_NullTargetUnit() {

        var a = new QuantityMeasurementApp.QuantityLength(
                1.0, QuantityMeasurementApp.LengthUnit.FEET);

        var b = new QuantityMeasurementApp.QuantityLength(
                12.0, QuantityMeasurementApp.LengthUnit.INCHES);

        assertThrows(IllegalArgumentException.class,
                () -> QuantityMeasurementApp.QuantityLength.add(a, b, null));
    }
}