package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.model.QuantityInputDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * QuantityMeasurementController - REST controller for all quantity measurement operations.
 * UC17: Full Spring Boot REST migration with Swagger docs.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/quantities")
@RequiredArgsConstructor
@Tag(name = "Quantity Measurement", description = "REST API for quantity comparison, conversion, and arithmetic")
public class QuantityMeasurementController {

    private final IQuantityMeasurementService service;

    // ---- Operations ----

    @PostMapping("/compare")
    @Operation(summary = "Compare two quantities", description = "Returns true if two quantities of the same type are equal within tolerance")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Comparison result returned"),
        @ApiResponse(responseCode = "400", description = "Validation or business logic error")
    })
    public ResponseEntity<QuantityMeasurementDTO> compare(@Valid @RequestBody QuantityInputDTO input) {
        log.debug("REST POST /compare");
        return ResponseEntity.ok(service.compare(input));
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert a quantity to its base unit")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Conversion result returned"),
        @ApiResponse(responseCode = "400", description = "Validation or business logic error")
    })
    public ResponseEntity<QuantityMeasurementDTO> convert(@Valid @RequestBody QuantityInputDTO input) {
        log.debug("REST POST /convert");
        return ResponseEntity.ok(service.convert(input));
    }

    @PostMapping("/add")
    @Operation(summary = "Add two quantities", description = "Returns the sum in the base unit")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Addition result returned"),
        @ApiResponse(responseCode = "400", description = "Validation or business logic error")
    })
    public ResponseEntity<QuantityMeasurementDTO> add(@Valid @RequestBody QuantityInputDTO input) {
        log.debug("REST POST /add");
        return ResponseEntity.ok(service.add(input));
    }

    @PostMapping("/subtract")
    @Operation(summary = "Subtract two quantities", description = "Returns thisQuantity minus thatQuantity in the base unit")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Subtraction result returned"),
        @ApiResponse(responseCode = "400", description = "Validation or business logic error")
    })
    public ResponseEntity<QuantityMeasurementDTO> subtract(@Valid @RequestBody QuantityInputDTO input) {
        log.debug("REST POST /subtract");
        return ResponseEntity.ok(service.subtract(input));
    }

    @PostMapping("/multiply")
    @Operation(summary = "Multiply two quantities")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Multiplication result returned"),
        @ApiResponse(responseCode = "400", description = "Validation or business logic error")
    })
    public ResponseEntity<QuantityMeasurementDTO> multiply(@Valid @RequestBody QuantityInputDTO input) {
        log.debug("REST POST /multiply");
        return ResponseEntity.ok(service.multiply(input));
    }

    @PostMapping("/divide")
    @Operation(summary = "Divide two quantities")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Division result returned"),
        @ApiResponse(responseCode = "400", description = "Validation or business logic error"),
        @ApiResponse(responseCode = "500", description = "Division by zero")
    })
    public ResponseEntity<QuantityMeasurementDTO> divide(@Valid @RequestBody QuantityInputDTO input) {
        log.debug("REST POST /divide");
        return ResponseEntity.ok(service.divide(input));
    }

    // ---- History / Query ----

    @GetMapping("/history/operation/{operation}")
    @Operation(summary = "Get operation history by type")
    public ResponseEntity<List<QuantityMeasurementDTO>> getHistoryByOperation(
            @Parameter(description = "Operation type: COMPARE, CONVERT, ADD, SUBTRACT, MULTIPLY, DIVIDE")
            @PathVariable String operation) {
        return ResponseEntity.ok(service.getHistoryByOperation(operation));
    }

    @GetMapping("/history/type/{measurementType}")
    @Operation(summary = "Get operation history by measurement category")
    public ResponseEntity<List<QuantityMeasurementDTO>> getHistoryByType(
            @Parameter(description = "Measurement type: LENGTH, WEIGHT, VOLUME, TEMPERATURE")
            @PathVariable String measurementType) {
        return ResponseEntity.ok(service.getHistoryByType(measurementType));
    }

    @GetMapping("/history/errored")
    @Operation(summary = "Get all errored operations")
    public ResponseEntity<List<QuantityMeasurementDTO>> getErrorHistory() {
        return ResponseEntity.ok(service.getErrorHistory());
    }

    @GetMapping("/count/{operation}")
    @Operation(summary = "Count successful operations by type")
    public ResponseEntity<Long> countByOperation(
            @Parameter(description = "Operation type to count")
            @PathVariable String operation) {
        return ResponseEntity.ok(service.countByOperation(operation));
    }
}
