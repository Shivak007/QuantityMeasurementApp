package com.bridgelabz;

/**
 * QuantityMeasurementApp provides comparison and conversion
 * functionality for length measurements.
 * Base unit chosen: FEET
 */
public class QuantityMeasurementApp {
    // ENUM: LengthUnit
    public enum LengthUnit {

        FEET(1.0),
        INCH(1.0 / 12.0),
        YARDS(3.0),
        CENTIMETERS(0.393701 / 12.0);

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }

        public double fromFeet(double feetValue) {
            return feetValue / toFeetFactor;
        }
    }

    // CLASS: QuantityLength
    public static class QuantityLength {

        private final double value;
        private final LengthUnit unit;

        private static final double EPSILON = 1e-6;

        public QuantityLength(double value, LengthUnit unit) {
            validate(value, unit);
            this.value = value;
            this.unit = unit;
        }

        private void validate(double value, LengthUnit unit) {
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Invalid numeric value");
            }
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
        }

        private double toBaseUnit() {
            return unit.toFeet(value);
        }

        //Static conversion API
        public static double convert(double value,
                                     LengthUnit source,
                                     LengthUnit target) {

            if (!Double.isFinite(value))
                throw new IllegalArgumentException("Invalid numeric value");

            if (source == null || target == null)
                throw new IllegalArgumentException("Units cannot be null");

            double valueInFeet = source.toFeet(value);
            return target.fromFeet(valueInFeet);
        }

        //Instance conversion API
        public QuantityLength convertTo(LengthUnit target) {
            double convertedValue = convert(this.value, this.unit, target);
            return new QuantityLength(convertedValue, target);
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) return true;

            if (obj == null || getClass() != obj.getClass())
                return false;

            QuantityLength other = (QuantityLength) obj;

            return Math.abs(
                    this.toBaseUnit() - other.toBaseUnit()
            ) < EPSILON;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(toBaseUnit());
        }

        @Override
        public String toString() {
            return "Quantity(" + value + ", " + unit + ")";
        }
    }

    // Overloaded Demo Methods
    public static void demonstrateLengthConversion(
            double value,
            LengthUnit from,
            LengthUnit to) {

        double result =
                QuantityLength.convert(value, from, to);

        System.out.println(
                "convert(" + value + ", " + from + ", " + to + ") → "
                        + result);
    }

    public static void demonstrateLengthConversion(
            QuantityLength length,
            LengthUnit to) {

        QuantityLength converted =
                length.convertTo(to);

        System.out.println(
                length + " converted to " + to + " → "
                        + converted);
    }

    public static void demonstrateLengthEquality(
            QuantityLength a,
            QuantityLength b) {

        System.out.println(a + " == " + b + " ? "
                + a.equals(b));
    }

    // MAIN
    public static void main(String[] args) {

        demonstrateLengthConversion(1.0,
                LengthUnit.FEET,
                LengthUnit.INCH);

        demonstrateLengthConversion(3.0,
                LengthUnit.YARDS,
                LengthUnit.FEET);

        demonstrateLengthConversion(36.0,
                LengthUnit.INCH,
                LengthUnit.YARDS);

        demonstrateLengthConversion(1.0,
                LengthUnit.CENTIMETERS,
                LengthUnit.INCH);

        demonstrateLengthConversion(0.0,
                LengthUnit.FEET,
                LengthUnit.INCH);
    }
}