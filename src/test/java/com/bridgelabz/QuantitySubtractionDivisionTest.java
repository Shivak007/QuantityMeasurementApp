package com.bridgelabz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantitySubtractionDivisionTest {

    private static final double EPSILON = 1e-6;

    @Test
    void testSubtraction_CrossUnit() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(6.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result =
                a.subtract(b);

        assertEquals(9.5, result.getValue(), EPSILON);
    }

    @Test
    void testSubtraction_ExplicitTargetUnit() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(6.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result =
                a.subtract(b, LengthUnit.INCHES);

        assertEquals(114.0, result.getValue(), EPSILON);
    }

    @Test
    void testSubtraction_ResultZero() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(120.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result =
                a.subtract(b);

        assertEquals(0.0, result.getValue(), EPSILON);
    }

    @Test
    void testDivision_SameUnit() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(2.0, LengthUnit.FEET);

        assertEquals(5.0, a.divide(b));
    }

    @Test
    void testDivision_CrossUnit() {

        Quantity<LengthUnit> a =
                new Quantity<>(24.0, LengthUnit.INCHES);

        Quantity<LengthUnit> b =
                new Quantity<>(2.0, LengthUnit.FEET);

        assertEquals(1.0, a.divide(b), EPSILON);
    }

    @Test
    void testDivision_ByZero() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(0.0, LengthUnit.FEET);

        assertThrows(ArithmeticException.class,
                () -> a.divide(b));
    }
}