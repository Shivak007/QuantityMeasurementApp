package com.bridgelabz;

public class QuantityMeasurementApp {
    // ENUM: LengthUnit
    // Base Unit: FEET
    public enum LengthUnit {

        FEET(1.0),
        INCHES(1.0 / 12.0),
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

            if (!Double.isFinite(value))
                throw new IllegalArgumentException("Invalid numeric value");

            if (unit == null)
                throw new IllegalArgumentException("Unit cannot be null");
        }

        private double toBaseUnit() {
            return unit.toFeet(value);
        }

        public double getValue() {
            return value;
        }

        public LengthUnit getUnit() {
            return unit;
        }

        // UC6 — Implicit Addition (returns in first operand unit)
        public QuantityLength add(QuantityLength other) {

            if (other == null)
                throw new IllegalArgumentException("Second operand cannot be null");

            return add(this, other, this.unit);
        }

        // UC7 — Explicit Target Unit Addition
        public static QuantityLength add(
                QuantityLength a,
                QuantityLength b,
                LengthUnit targetUnit) {

            if (a == null || b == null)
                throw new IllegalArgumentException("Operands cannot be null");

            if (targetUnit == null)
                throw new IllegalArgumentException("Target unit cannot be null");

            if (!Double.isFinite(a.value) || !Double.isFinite(b.value))
                throw new IllegalArgumentException("Invalid numeric value");

            // Convert both to base unit (feet)
            double sumInFeet =
                    a.toBaseUnit() + b.toBaseUnit();

            // Convert base unit sum to target unit
            double resultValue =
                    targetUnit.fromFeet(sumInFeet);

            return new QuantityLength(resultValue, targetUnit);
        }

        // equals() based on base-unit comparison
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

    // MAIN
    public static void main(String[] args) {

        QuantityLength a =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength b =
                new QuantityLength(12.0, LengthUnit.INCHES);

        System.out.println(
                QuantityLength.add(a, b, LengthUnit.FEET));

        System.out.println(
                QuantityLength.add(a, b, LengthUnit.INCHES));

        System.out.println(
                QuantityLength.add(a, b, LengthUnit.YARDS));
    }
}