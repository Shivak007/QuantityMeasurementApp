package com.bridgelabz;

/**
 * QuantityLength handles equality, conversion and arithmetic.
 * Conversion responsibility delegated to LengthUnit.
 */
public class QuantityLength {

    private final double value;
    private final LengthUnit unit;

    private static final double EPSILON = 1e-6;

    public QuantityLength(double value, LengthUnit unit) {

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
        return unit.convertToBaseUnit(value);
    }

    // ===============================
    // Conversion
    // ===============================
    public QuantityLength convertTo(LengthUnit targetUnit) {

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double baseValue = unit.convertToBaseUnit(value);
        double convertedValue = targetUnit.convertFromBaseUnit(baseValue);

        return new QuantityLength(convertedValue, targetUnit);
    }

    // ===============================
    // UC6 – Implicit Add
    // ===============================
    public QuantityLength add(QuantityLength other) {

        if (other == null)
            throw new IllegalArgumentException("Operand cannot be null");

        return add(this, other, this.unit);
    }

    // ===============================
    // UC7 – Explicit Add
    // ===============================
    public static QuantityLength add(
            QuantityLength a,
            QuantityLength b,
            LengthUnit targetUnit) {

        if (a == null || b == null)
            throw new IllegalArgumentException("Operands cannot be null");

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double sumInBase =
                a.unit.convertToBaseUnit(a.value)
                        + b.unit.convertToBaseUnit(b.value);

        double resultValue =
                targetUnit.convertFromBaseUnit(sumInBase);

        return new QuantityLength(resultValue, targetUnit);
    }

    // ===============================
    // Equality
    // ===============================
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