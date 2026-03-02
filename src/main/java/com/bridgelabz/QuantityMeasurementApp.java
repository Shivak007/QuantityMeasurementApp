package com.bridgelabz;

public class QuantityMeasurementApp {
    // ENUM: LengthUnit (Extended in UC4)
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
    }

    // CLASS: QuantityLength
    public static class QuantityLength {

        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            validate(value, unit);
            this.value = value;
            this.unit = unit;
        }

        private void validate(double value, LengthUnit unit) {
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                throw new IllegalArgumentException("Invalid numeric value");
            }
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
        }

        private double toBaseUnit() {
            return unit.toFeet(value);
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) return true;

            if (obj == null || getClass() != obj.getClass())
                return false;

            QuantityLength other = (QuantityLength) obj;

            return Double.compare(
                    this.toBaseUnit(),
                    other.toBaseUnit()) == 0;
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

    // MAIN DEMO
    public static void main(String[] args) {

        QuantityLength yard =
                new QuantityLength(1.0, LengthUnit.YARDS);

        QuantityLength feet =
                new QuantityLength(3.0, LengthUnit.FEET);

        QuantityLength inches =
                new QuantityLength(36.0, LengthUnit.INCH);

        QuantityLength cm =
                new QuantityLength(1.0, LengthUnit.CENTIMETERS);

        QuantityLength inchEquivalent =
                new QuantityLength(0.393701, LengthUnit.INCH);

        System.out.println("1 Yard == 3 Feet ? " + yard.equals(feet));
        System.out.println("1 Yard == 36 Inches ? " + yard.equals(inches));
        System.out.println("1 cm == 0.393701 inch ? " + cm.equals(inchEquivalent));
    }
}