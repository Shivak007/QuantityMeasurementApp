package com.bridgelabz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityLengthTest {

    private static final double EPSILON = 1e-6;
    // LengthUnit ENUM TESTS
    @Test
    void testLengthUnit_FeetConstant() {
        assertEquals(1.0,
                LengthUnit.FEET.getConversionFactor(),
                EPSILON);
    }

    @Test
    void testLengthUnit_InchesConstant() {
        assertEquals(1.0 / 12.0,
                LengthUnit.INCHES.getConversionFactor(),
                EPSILON);
    }

    @Test
    void testLengthUnit_YardsConstant() {
        assertEquals(3.0,
                LengthUnit.YARDS.getConversionFactor(),
                EPSILON);
    }

    @Test
    void testLengthUnit_CentimetersConstant() {
        assertEquals(1.0 / 30.48,
                LengthUnit.CENTIMETERS.getConversionFactor(),
                EPSILON);
    }

    // convertToBaseUnit TESTS
    @Test
    void testConvertToBaseUnit_InchesToFeet() {
        assertEquals(1.0,
                LengthUnit.INCHES.convertToBaseUnit(12.0),
                EPSILON);
    }

    @Test
    void testConvertToBaseUnit_YardsToFeet() {
        assertEquals(3.0,
                LengthUnit.YARDS.convertToBaseUnit(1.0),
                EPSILON);
    }

    @Test
    void testConvertToBaseUnit_CentimetersToFeet() {
        assertEquals(1.0,
                LengthUnit.CENTIMETERS.convertToBaseUnit(30.48),
                EPSILON);
    }

    // convertFromBaseUnit TESTS
    @Test
    void testConvertFromBaseUnit_FeetToInches() {
        assertEquals(12.0,
                LengthUnit.INCHES.convertFromBaseUnit(1.0),
                EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_FeetToYards() {
        assertEquals(1.0,
                LengthUnit.YARDS.convertFromBaseUnit(3.0),
                EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_FeetToCentimeters() {
        assertEquals(30.48,
                LengthUnit.CENTIMETERS.convertFromBaseUnit(1.0),
                EPSILON);
    }

    // QuantityLength – Equality
    @Test
    void testEquality_CrossUnit() {

        var feet = new QuantityLength(1.0, LengthUnit.FEET);
        var inches = new QuantityLength(12.0, LengthUnit.INCHES);

        assertTrue(feet.equals(inches));
    }

    // Conversion
    @Test
    void testConvertTo_Method() {

        var feet = new QuantityLength(1.0, LengthUnit.FEET);

        var result = feet.convertTo(LengthUnit.INCHES);

        assertEquals(12.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }

    // Addition – Explicit Target (UC7 compatibility)
    @Test
    void testAdd_WithTargetUnit() {

        var a = new QuantityLength(1.0, LengthUnit.FEET);
        var b = new QuantityLength(12.0, LengthUnit.INCHES);

        var result = QuantityLength.add(a, b, LengthUnit.FEET);

        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    void testAdd_WithTargetUnit_Yards() {

        var a = new QuantityLength(1.0, LengthUnit.FEET);
        var b = new QuantityLength(12.0, LengthUnit.INCHES);

        var result = QuantityLength.add(a, b, LengthUnit.YARDS);

        assertEquals(0.6666667, result.getValue(), 1e-4);
    }

    // Validation
    @Test
    void testInvalidValue_NaN() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityLength(Double.NaN, LengthUnit.FEET));
    }

    @Test
    void testNullUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityLength(1.0, null));
    }

    @Test
    void testNullTargetUnit() {

        var a = new QuantityLength(1.0, LengthUnit.FEET);
        var b = new QuantityLength(1.0, LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.add(a, b, null));
    }

    // Round Trip Conversion Precision

    @Test
    void testRoundTripConversion() {

        var original = new QuantityLength(1.0, LengthUnit.FEET);

        var inches = original.convertTo(LengthUnit.INCHES);
        var backToFeet = inches.convertTo(LengthUnit.FEET);

        assertEquals(original.getValue(),
                backToFeet.getValue(),
                EPSILON);
    }
}