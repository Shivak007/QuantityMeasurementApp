package com.bridgelabz;

public class QuantityMeasurementApp {

    public static class Length {

        private final double value;
        private final LengthUnit unit;
        private static final double EPSILON = 1e-6;

        public Length(double value, LengthUnit unit) {
            if (!Double.isFinite(value))
                throw new IllegalArgumentException("Invalid numeric value");
            if (unit == null)
                throw new IllegalArgumentException("Unit cannot be null");

            this.value = value;
            this.unit = unit;
        }

        public double getValue() {
            return value;
        }

        public LengthUnit getUnit() {
            return unit;
        }

        private double toBaseUnit() {
            return value * unit.getFactor();
        }

        public Length convertTo(LengthUnit targetUnit) {
            if (targetUnit == null)
                throw new IllegalArgumentException("Target unit cannot be null");

            double baseValue = toBaseUnit();
            double converted = baseValue / targetUnit.getFactor();
            return new Length(converted, targetUnit);
        }

        public Length add(Length other) {
            if (other == null)
                throw new IllegalArgumentException("Length to add cannot be null");

            double sumBase =
                    this.toBaseUnit() + other.toBaseUnit();

            double result =
                    sumBase / this.unit.getFactor();

            return new Length(result, this.unit);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Length)) return false;

            Length other = (Length) obj;
            return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < EPSILON;
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    public enum LengthUnit {
        FEET(12.0),
        INCHES(1.0),
        YARDS(36.0),
        CENTIMETERS(0.393701);

        private final double factor;

        LengthUnit(double factor) {
            this.factor = factor;
        }

        public double getFactor() {
            return factor;
        }
    }

    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Invalid numeric value");
        if (source == null || target == null)
            throw new IllegalArgumentException("Unit cannot be null");

        double baseValue = value * source.getFactor();
        return baseValue / target.getFactor();
    }

    public static Length demonstrateLengthAddition(Length l1, Length l2) {
        return l1.add(l2);
    }

    public static void main(String[] args) {
        Length a = new Length(1.0, LengthUnit.FEET);
        Length b = new Length(12.0, LengthUnit.INCHES);
        System.out.println(a.add(b));
    }
}