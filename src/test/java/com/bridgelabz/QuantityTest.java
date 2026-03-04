package com.bridgelabz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityTest {

    private static final double EPSILON = 1e-6;

    // ===============================
    // LENGTH EQUALITY TESTS
    // ===============================

    @Test
    void testLengthEquality_FeetAndInches() {

        Quantity<LengthUnit> feet =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> inches =
                new Quantity<>(12.0, LengthUnit.INCHES);

        assertTrue(feet.equals(inches));
    }

    @Test
    void testLengthEquality_YardAndFeet() {

        Quantity<LengthUnit> yard =
                new Quantity<>(1.0, LengthUnit.YARDS);

        Quantity<LengthUnit> feet =
                new Quantity<>(3.0, LengthUnit.FEET);

        assertTrue(yard.equals(feet));
    }

    @Test
    void testLengthEquality_CentimeterAndInch() {

        Quantity<LengthUnit> cm =
                new Quantity<>(2.54, LengthUnit.CENTIMETERS);

        Quantity<LengthUnit> inch =
                new Quantity<>(1.0, LengthUnit.INCHES);

        assertTrue(cm.equals(inch));
    }

    // ===============================
    // WEIGHT EQUALITY TESTS
    // ===============================

    @Test
    void testWeightEquality_KgAndGram() {

        Quantity<WeightUnit> kg =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> gram =
                new Quantity<>(1000.0, WeightUnit.GRAM);

        assertTrue(kg.equals(gram));
    }

    @Test
    void testWeightEquality_KgAndPound() {

        Quantity<WeightUnit> kg =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> pound =
                new Quantity<>(2.20462, WeightUnit.POUND);

        assertTrue(kg.equals(pound));
    }

    // ===============================
    // CROSS CATEGORY PREVENTION
    // ===============================

    @Test
    void testCrossCategoryComparison() {

        Quantity<LengthUnit> length =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<WeightUnit> weight =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        assertFalse(length.equals(weight));
    }

    // ===============================
    // CONVERSION TESTS
    // ===============================

    @Test
    void testLengthConversion_FeetToInches() {

        Quantity<LengthUnit> feet =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> inches =
                feet.convertTo(LengthUnit.INCHES);

        assertEquals(12.0, inches.getValue(), EPSILON);
    }

    @Test
    void testWeightConversion_KgToGram() {

        Quantity<WeightUnit> kg =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> gram =
                kg.convertTo(WeightUnit.GRAM);

        assertEquals(1000.0, gram.getValue(), EPSILON);
    }

    // ===============================
    // ADDITION TESTS
    // ===============================

    @Test
    void testLengthAddition() {

        Quantity<LengthUnit> a =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(12.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result =
                a.add(b, LengthUnit.FEET);

        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    void testWeightAddition() {

        Quantity<WeightUnit> a =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> b =
                new Quantity<>(1000.0, WeightUnit.GRAM);

        Quantity<WeightUnit> result =
                a.add(b, WeightUnit.KILOGRAM);

        assertEquals(2.0, result.getValue(), EPSILON);
    }

    // ===============================
    // CONSTRUCTOR VALIDATION
    // ===============================

    @Test
    void testConstructorValidation_NullUnit() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Quantity<>(1.0, null)
        );
    }

    @Test
    void testConstructorValidation_InvalidValue() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Quantity<>(Double.NaN, LengthUnit.FEET)
        );
    }

    // ===============================
    // HASHCODE CONSISTENCY
    // ===============================

    @Test
    void testHashCodeConsistency() {

        Quantity<LengthUnit> a =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(12.0, LengthUnit.INCHES);

        assertEquals(a.hashCode(), b.hashCode());
    }

    // ===============================
    // ROUND TRIP CONVERSION
    // ===============================

    @Test
    void testRoundTripConversion() {

        Quantity<LengthUnit> original =
                new Quantity<>(5.0, LengthUnit.FEET);

        Quantity<LengthUnit> converted =
                original.convertTo(LengthUnit.INCHES)
                        .convertTo(LengthUnit.FEET);

        assertEquals(original.getValue(), converted.getValue(), EPSILON);
    }
}