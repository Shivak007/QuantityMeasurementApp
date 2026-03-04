package com.bridgelabz;

public class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {

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

    public U getUnit() {
        return unit;
    }

    private double toBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    // Convert
    public Quantity<U> convertTo(U targetUnit) {

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double base = unit.convertToBaseUnit(value);
        double result = targetUnit.convertFromBaseUnit(base);

        return new Quantity<>(result, targetUnit);
    }

    // Addition (implicit unit)
    public Quantity<U> add(Quantity<U> other) {

        if (other == null)
            throw new IllegalArgumentException("Other cannot be null");

        return add(other, this.unit);
    }

    // Addition (explicit unit)
    public Quantity<U> add(Quantity<U> other, U targetUnit) {

        if (other == null)
            throw new IllegalArgumentException("Other cannot be null");

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double sumBase =
                this.toBaseUnit() + other.toBaseUnit();

        double result =
                targetUnit.convertFromBaseUnit(sumBase);

        return new Quantity<>(result, targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other) {

        if (other == null)
            throw new IllegalArgumentException("Operand cannot be null");

        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {

        if (other == null)
            throw new IllegalArgumentException("Operand cannot be null");

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        if (this.unit.getClass() != other.unit.getClass())
            throw new IllegalArgumentException("Cross-category subtraction not allowed");

        double baseResult =
                this.unit.convertToBaseUnit(value) -
                        other.unit.convertToBaseUnit(other.value);

        double converted =
                targetUnit.convertFromBaseUnit(baseResult);

        double rounded =
                Math.round(converted * 100.0) / 100.0;

        return new Quantity<>(rounded, targetUnit);
    }

    public double divide(Quantity<U> other) {

        if (other == null)
            throw new IllegalArgumentException("Operand cannot be null");

        if (this.unit.getClass() != other.unit.getClass())
            throw new IllegalArgumentException("Cross-category division not allowed");

        double divisor =
                other.unit.convertToBaseUnit(other.value);

        if (divisor == 0)
            throw new ArithmeticException("Division by zero");

        double dividend =
                this.unit.convertToBaseUnit(this.value);

        return dividend / divisor;
    }


    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (!(obj instanceof Quantity<?> other))
            return false;

        // prevent cross-category comparison
        if (this.unit.getClass() != other.unit.getClass())
            return false;

        double a = this.toBaseUnit();
        double b = other.toBaseUnit();

        return Math.abs(a - b) < 1e-5;
    }


    @Override
    public int hashCode() {
        return Double.hashCode(toBaseUnit());
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit.getUnitName() + ")";
    }
}