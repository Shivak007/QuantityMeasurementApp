package com.bridgelabz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest {

    private static final double EPSILON = 1e-6;

    @Test
    void testFeetEquality() {
        var a = new QuantityMeasurementApp.Length(1.0, LengthUnit.FEET);
        var b = new QuantityMeasurementApp.Length(1.0, LengthUnit.FEET);
        assertTrue(a.equals(b));
    }

    @Test
    void testFeetInchesEquality() {
        var a = new QuantityMeasurementApp.Length(1.0, LengthUnit.FEET);
        var b = new QuantityMeasurementApp.Length(12.0, LengthUnit.INCHES);
        assertTrue(a.equals(b));
    }

    @Test
    void testYardEqualsThreeFeet() {
        var yard = new QuantityMeasurementApp.Length(1.0, LengthUnit.YARDS);
        var feet = new QuantityMeasurementApp.Length(3.0, LengthUnit.FEET);
        assertTrue(yard.equals(feet));
    }

    @Test
    void testCentimeterEqualsInches() {
        var cm = new QuantityMeasurementApp.Length(2.54, LengthUnit.CENTIMETERS);
        var inch = new QuantityMeasurementApp.Length(1.0, LengthUnit.INCHES);
        assertTrue(cm.equals(inch));
    }

    @Test
    void testConvertFeetToInches() {
        var feet = new QuantityMeasurementApp.Length(1.0, LengthUnit.FEET);
        var inches = feet.convertTo(LengthUnit.INCHES);
        assertEquals(12.0, inches.getValue(), EPSILON);
    }

    @Test
    void testConvertYardToFeet() {
        var yard = new QuantityMeasurementApp.Length(1.0, LengthUnit.YARDS);
        var feet = yard.convertTo(LengthUnit.FEET);
        assertEquals(3.0, feet.getValue(), EPSILON);
    }

    @Test
    void testAdditionDefaultUnit() {
        var a = new QuantityMeasurementApp.Length(1.0, LengthUnit.FEET);
        var b = new QuantityMeasurementApp.Length(12.0, LengthUnit.INCHES);
        var result = a.add(b);
        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testAdditionWithTargetUnitFeet() {
        var a = new QuantityMeasurementApp.Length(1.0, LengthUnit.FEET);
        var b = new QuantityMeasurementApp.Length(12.0, LengthUnit.INCHES);
        var result = a.add(b, LengthUnit.FEET);
        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    void testAdditionWithTargetUnitInches() {
        var a = new QuantityMeasurementApp.Length(1.0, LengthUnit.FEET);
        var b = new QuantityMeasurementApp.Length(12.0, LengthUnit.INCHES);
        var result = a.add(b, LengthUnit.INCHES);
        assertEquals(24.0, result.getValue(), EPSILON);
    }

    @Test
    void testAdditionWithTargetUnitYards() {
        var a = new QuantityMeasurementApp.Length(1.0, LengthUnit.FEET);
        var b = new QuantityMeasurementApp.Length(12.0, LengthUnit.INCHES);
        var result = a.add(b, LengthUnit.YARDS);
        assertEquals(2.0 / 3.0, result.getValue(), EPSILON);
    }

    @Test
    void testCommutativity() {
        var a = new QuantityMeasurementApp.Length(1.0, LengthUnit.FEET);
        var b = new QuantityMeasurementApp.Length(12.0, LengthUnit.INCHES);
        var r1 = a.add(b, LengthUnit.YARDS);
        var r2 = b.add(a, LengthUnit.YARDS);
        assertEquals(r1.getValue(), r2.getValue(), EPSILON);
    }

    @Test
    void testZeroAddition() {
        var a = new QuantityMeasurementApp.Length(5.0, LengthUnit.FEET);
        var zero = new QuantityMeasurementApp.Length(0.0, LengthUnit.INCHES);
        var result = a.add(zero);
        assertEquals(5.0, result.getValue(), EPSILON);
    }

    @Test
    void testNegativeAddition() {
        var a = new QuantityMeasurementApp.Length(5.0, LengthUnit.FEET);
        var b = new QuantityMeasurementApp.Length(-2.0, LengthUnit.FEET);
        var result = a.add(b, LengthUnit.INCHES);
        assertEquals(36.0, result.getValue(), EPSILON);
    }

    @Test
    void testNullUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityMeasurementApp.Length(1.0, null));
    }

    @Test
    void testInvalidValue() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityMeasurementApp.Length(Double.NaN, LengthUnit.FEET));
    }

    @Test
    void testRoundTripConversion() {
        var original = new QuantityMeasurementApp.Length(5.0, LengthUnit.FEET);
        var converted = original.convertTo(LengthUnit.INCHES)
                .convertTo(LengthUnit.FEET);
        assertEquals(original.getValue(), converted.getValue(), EPSILON);
    }
}