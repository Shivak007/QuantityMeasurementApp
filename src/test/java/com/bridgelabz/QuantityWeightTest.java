package com.bridgelabz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityWeightTest {

    private static final double EPSILON = 1e-6;
    // Equality Tests
    @Test
    void testEquality_KilogramToKilogram_SameValue() {
        assertTrue(
                new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                        .equals(new QuantityWeight(1.0, WeightUnit.KILOGRAM))
        );
    }

    @Test
    void testEquality_KilogramToGram_Equivalent() {
        assertTrue(
                new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                        .equals(new QuantityWeight(1000.0, WeightUnit.GRAM))
        );
    }

    @Test
    void testEquality_KilogramToPound_Equivalent() {
        assertTrue(
                new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                        .equals(new QuantityWeight(2.20462, WeightUnit.POUND))
        );
    }

    @Test
    void testEquality_NullComparison() {
        assertFalse(
                new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                        .equals(null)
        );
    }

    // Conversion Tests
    @Test
    void testConversion_KilogramToGram() {
        var result =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                        .convertTo(WeightUnit.GRAM);

        assertEquals(1000.0, result.getValue(), EPSILON);
    }

    @Test
    void testConversion_PoundToKilogram() {
        var result =
                new QuantityWeight(2.20462, WeightUnit.POUND)
                        .convertTo(WeightUnit.KILOGRAM);

        assertEquals(1.0, result.getValue(), 1e-3);
    }

    @Test
    void testConversion_RoundTrip() {

        var original =
                new QuantityWeight(1.5, WeightUnit.KILOGRAM);

        var roundTrip =
                original.convertTo(WeightUnit.GRAM)
                        .convertTo(WeightUnit.KILOGRAM);

        assertEquals(original.getValue(),
                roundTrip.getValue(),
                EPSILON);
    }

    // Addition Tests
    @Test
    void testAddition_SameUnit() {

        var result =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                        .add(new QuantityWeight(2.0, WeightUnit.KILOGRAM));

        assertEquals(3.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_CrossUnit() {

        var result =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                        .add(new QuantityWeight(1000.0, WeightUnit.GRAM));

        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_ExplicitTarget() {

        var result =
                QuantityWeight.add(
                        new QuantityWeight(1.0, WeightUnit.KILOGRAM),
                        new QuantityWeight(1000.0, WeightUnit.GRAM),
                        WeightUnit.GRAM);

        assertEquals(2000.0, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_NegativeValues() {

        var result =
                new QuantityWeight(5.0, WeightUnit.KILOGRAM)
                        .add(new QuantityWeight(-2000.0, WeightUnit.GRAM));

        assertEquals(3.0, result.getValue(), EPSILON);
    }
}