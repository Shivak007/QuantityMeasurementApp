package com.bridgelabz;

public class QuantityMeasurementApp {
    // Static Equality Methods
    public static boolean checkFeetEquality(double value1, double value2) {
        Feet f1 = new Feet(value1);
        Feet f2 = new Feet(value2);
        return f1.equals(f2);
    }

    public static boolean checkInchEquality(double value1, double value2) {
        Inches i1 = new Inches(value1);
        Inches i2 = new Inches(value2);
        return i1.equals(i2);
    }

    // FEET CLASS
    public static class Feet {

        private final double value;

        public Feet(double value) {
            validateNumeric(value);
            this.value = value;
        }

        private void validateNumeric(double value) {
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                throw new IllegalArgumentException("Invalid numeric value for Feet");
            }
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) return true;

            if (obj == null || getClass() != obj.getClass())
                return false;

            Feet other = (Feet) obj;

            return Double.compare(this.value, other.value) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }
    }

    // INCHES CLASS
    public static class Inches {

        private final double value;

        public Inches(double value) {
            validateNumeric(value);
            this.value = value;
        }

        private void validateNumeric(double value) {
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                throw new IllegalArgumentException("Invalid numeric value for Inches");
            }
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) return true;

            if (obj == null || getClass() != obj.getClass())
                return false;

            Inches other = (Inches) obj;

            return Double.compare(this.value, other.value) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }
    }
    // MAIN METHOD
    public static void main(String[] args) {

        System.out.println("Input: 1.0 ft and 1.0 ft");
        System.out.println("Output: Equal (" +
                checkFeetEquality(1.0, 1.0) + ")");

        System.out.println("Input: 1.0 inch and 1.0 inch");
        System.out.println("Output: Equal (" +
                checkInchEquality(1.0, 1.0) + ")");
    }
}