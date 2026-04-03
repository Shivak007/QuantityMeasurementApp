package com.bridgelabz;

public class UnitFactory {

    public static IMeasurable getUnit(String unit) {

        switch (unit.toUpperCase()) {
            case "FEET": return LengthUnit.FEET;

            case "INCH":
            case "INCHES":
                return LengthUnit.INCHES;

            case "YARDS": return LengthUnit.YARDS;
            case "CENTIMETERS": return LengthUnit.CENTIMETERS;

            case "KILOGRAM": return WeightUnit.KILOGRAM;
            case "GRAM": return WeightUnit.GRAM;
            case "POUND": return WeightUnit.POUND;

            case "LITRE": return VolumeUnit.LITRE;
            case "MILLILITRE": return VolumeUnit.MILLILITRE;
            case "GALLON": return VolumeUnit.GALLON;

            case "CELSIUS": return TemperatureUnit.CELSIUS;
            case "FAHRENHEIT": return TemperatureUnit.FAHRENHEIT;
            case "KELVIN": return TemperatureUnit.KELVIN;

            default:
                throw new IllegalArgumentException("Invalid unit: " + unit);
        }
    }
}