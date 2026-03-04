package com.bridgelabz;

public class QuantityWeight {

    private final double value;
    private final WeightUnit unit;

    private static final double EPSILON = 1e-5;

    public QuantityWeight(double value, WeightUnit unit) {

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

    public WeightUnit getUnit() {
        return unit;
    }

    private double toBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    // ===============================
    // Conversion
    // ===============================
    public QuantityWeight convertTo(WeightUnit targetUnit) {

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double baseValue = unit.convertToBaseUnit(value);
        double converted = targetUnit.convertFromBaseUnit(baseValue);

        return new QuantityWeight(converted, targetUnit);
    }

    // ===============================
    // Addition (Implicit target)
    // ===============================
    public QuantityWeight add(QuantityWeight other) {

        if (other == null)
            throw new IllegalArgumentException("Operand cannot be null");

        return add(this, other, this.unit);
    }

    // ===============================
    // Addition (Explicit target)
    // ===============================
    public static QuantityWeight add(
            QuantityWeight a,
            QuantityWeight b,
            WeightUnit targetUnit) {

        if (a == null || b == null)
            throw new IllegalArgumentException("Operands cannot be null");

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double sumInBase =
                a.toBaseUnit() + b.toBaseUnit();

        double result =
                targetUnit.convertFromBaseUnit(sumInBase);

        return new QuantityWeight(result, targetUnit);
    }

    // ===============================
    // Equality
    // ===============================
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        QuantityWeight other = (QuantityWeight) obj;

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