package com.app.quantitymeasurement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * QuantityMeasurementEntity - JPA entity for persisting quantity measurement operations.
 * Migrated from UC16 plain POJO to JPA entity with extended fields.
 */
@Entity
@Table(name = "quantity_measurement_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityMeasurementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "this_value")
    private Double thisValue;

    @Column(name = "this_unit", length = 50)
    private String thisUnit;

    @Column(name = "this_measurement_type", length = 50)
    private String thisMeasurementType;

    @Column(name = "that_value")
    private Double thatValue;

    @Column(name = "that_unit", length = 50)
    private String thatUnit;

    @Column(name = "that_measurement_type", length = 50)
    private String thatMeasurementType;

    @Column(name = "operation", length = 20)
    private String operation;

    @Column(name = "result_string", length = 255)
    private String resultString;

    @Column(name = "result_value")
    private Double resultValue;

    @Column(name = "result_unit", length = 50)
    private String resultUnit;

    @Column(name = "result_measurement_type", length = 50)
    private String resultMeasurementType;

    @Column(name = "error_message", length = 500)
    private String errorMessage;

    @Column(name = "is_error")
    private Boolean error = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.error == null) this.error = false;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
