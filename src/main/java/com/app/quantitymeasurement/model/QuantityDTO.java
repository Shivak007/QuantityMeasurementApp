package com.app.quantitymeasurement.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * QuantityDTO - Represents a single quantity (value + unit + measurement type).
 * Used as part of REST request bodies with validation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityDTO {

    @NotNull(message = "Value must not be null")
    private Double value;

    @NotBlank(message = "Unit must not be blank")
    @Pattern(
        regexp = "(?i)INCH|FEET|YARD|CENTIMETER|GRAM|KILOGRAM|TONNE|LITRE|GALLON|ML|CELSIUS|FAHRENHEIT|KELVIN",
        message = "Unit must be one of: INCH, FEET, YARD, CENTIMETER, GRAM, KILOGRAM, TONNE, LITRE, GALLON, ML, CELSIUS, FAHRENHEIT, KELVIN"
    )
    private String unit;

    @NotBlank(message = "Measurement type must not be blank")
    @Pattern(
        regexp = "(?i)LENGTH|WEIGHT|VOLUME|TEMPERATURE",
        message = "Measurement type must be one of: LENGTH, WEIGHT, VOLUME, TEMPERATURE"
    )
    private String measurementType;
}
