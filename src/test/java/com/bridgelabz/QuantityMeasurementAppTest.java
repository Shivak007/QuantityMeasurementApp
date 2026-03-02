package com.bridgelabz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest {

    private static final double EPSILON = 1e-6;

    @Test
    void testAddition_SameUnit_Feet() {
        QuantityMeasurementApp.Length a =
                new QuantityMeasurementApp.Length(1.0,
                        QuantityMeasurementApp.LengthUnit.FEET);

        QuantityMeasurementApp.Length b =
                new QuantityMeasurementApp.Length(2.0,
                        QuantityMeasurementApp.LengthUnit.FEET);

        QuantityMeasurementApp.Length result = a.add(b);

        assertEquals(3.0, result.getValue(), EPSILON);
        assertEquals(QuantityMeasurementApp.LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testAddition_CrossUnit_FeetPlusInches() {
        QuantityMeasurementApp.Length a =
                new QuantityMeasurementApp.Length(1.0,
                        QuantityMeasurementApp.LengthUnit.FEET);

        QuantityMeasurementApp.Length b =
                new QuantityMeasurementApp.Length(12.0,
                        QuantityMeasurementApp.LengthUnit.INCHES);

        QuantityMeasurementApp.Length result = a.add(b);

        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(QuantityMeasurementApp.LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testAddition_CrossUnit_InchesPlusFeet() {
        QuantityMeasurementApp.Length a =
                new QuantityMeasurementApp.Length(12.0,
                        QuantityMeasurementApp.LengthUnit.INCHES);

        QuantityMeasurementApp.Length b =
                new QuantityMeasurementApp.Length(1.0,
                        QuantityMeasurementApp.LengthUnit.FEET);

        QuantityMeasurementApp.Length result = a.add(b);

        assertEquals(24.0, result.getValue(), EPSILON);
        assertEquals(QuantityMeasurementApp.LengthUnit.INCHES, result.getUnit());
    }

    @Test
    void testAddition_YardsPlusFeet() {
        QuantityMeasurementApp.Length a =
                new QuantityMeasurementApp.Length(1.0,
                        QuantityMeasurementApp.LengthUnit.YARDS);

        QuantityMeasurementApp.Length b =
                new QuantityMeasurementApp.Length(3.0,
                        QuantityMeasurementApp.LengthUnit.FEET);

        QuantityMeasurementApp.Length result = a.add(b);

        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(QuantityMeasurementApp.LengthUnit.YARDS, result.getUnit());
    }

    @Test
    void testAddition_WithZero() {
        QuantityMeasurementApp.Length a =
                new QuantityMeasurementApp.Length(5.0,
                        QuantityMeasurementApp.LengthUnit.FEET);

        QuantityMeasurementApp.Length b =
                new QuantityMeasurementApp.Length(0.0,
                        QuantityMeasurementApp.LengthUnit.INCHES);

        QuantityMeasurementApp.Length result = a.add(b);

        assertEquals(5.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_NegativeValues() {
        QuantityMeasurementApp.Length a =
                new QuantityMeasurementApp.Length(5.0,
                        QuantityMeasurementApp.LengthUnit.FEET);

        QuantityMeasurementApp.Length b =
                new QuantityMeasurementApp.Length(-2.0,
                        QuantityMeasurementApp.LengthUnit.FEET);

        QuantityMeasurementApp.Length result = a.add(b);

        assertEquals(3.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_NullOperand_Throws() {
        QuantityMeasurementApp.Length a =
                new QuantityMeasurementApp.Length(1.0,
                        QuantityMeasurementApp.LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class, () -> a.add(null));
    }
}