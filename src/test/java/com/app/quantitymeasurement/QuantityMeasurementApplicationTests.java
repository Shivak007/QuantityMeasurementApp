package com.app.quantitymeasurement;

import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityInputDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * QuantityMeasurementApplicationTests - Full @SpringBootTest integration tests.
 * Tests: application context, REST endpoints, service logic, JPA + H2 persistence.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser
class QuantityMeasurementApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IQuantityMeasurementService service;

    @Autowired
    private QuantityMeasurementRepository repository;

    @BeforeEach
    void clearDb() {
        repository.deleteAll();
    }

    // ---- Context loads ----

    @Test
    void contextLoads() {
        assertThat(service).isNotNull();
        assertThat(repository).isNotNull();
    }

    // ---- JPA Persistence ----

    @Test
    void compare_persistsEntityToH2() {
        QuantityInputDTO input = new QuantityInputDTO(
            new QuantityDTO(1.0, "FEET", "LENGTH"),
            new QuantityDTO(12.0, "INCH", "LENGTH")
        );
        QuantityMeasurementDTO result = service.compare(input);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getOperation()).isEqualTo("COMPARE");
        assertThat(result.getResultString()).isEqualTo("true");
        assertThat(result.getError()).isFalse();

        List<QuantityMeasurementEntity> saved = repository.findAll();
        assertThat(saved).hasSize(1);
        assertThat(saved.get(0).getOperation()).isEqualTo("COMPARE");
    }

    @Test
    void add_persistsEntityToH2() {
        QuantityInputDTO input = new QuantityInputDTO(
            new QuantityDTO(1.0, "FEET", "LENGTH"),
            new QuantityDTO(12.0, "INCH", "LENGTH")
        );
        QuantityMeasurementDTO result = service.add(input);

        assertThat(result.getResultValue()).isEqualTo(24.0);
        assertThat(repository.findAll()).hasSize(1);
    }

    // ---- UC16 business logic preserved ----

    @Test
    void compare_1Feet_12Inch_shouldBeEqual() {
        QuantityMeasurementDTO result = service.compare(new QuantityInputDTO(
            new QuantityDTO(1.0, "FEET", "LENGTH"),
            new QuantityDTO(12.0, "INCH", "LENGTH")
        ));
        assertThat(result.getResultString()).isEqualTo("true");
    }

    @Test
    void compare_1Yard_3Feet_shouldBeEqual() {
        QuantityMeasurementDTO result = service.compare(new QuantityInputDTO(
            new QuantityDTO(1.0, "YARD", "LENGTH"),
            new QuantityDTO(3.0, "FEET", "LENGTH")
        ));
        assertThat(result.getResultString()).isEqualTo("true");
    }

    @Test
    void compare_1Kg_1000Gram_shouldBeEqual() {
        QuantityMeasurementDTO result = service.compare(new QuantityInputDTO(
            new QuantityDTO(1.0, "KILOGRAM", "WEIGHT"),
            new QuantityDTO(1000.0, "GRAM", "WEIGHT")
        ));
        assertThat(result.getResultString()).isEqualTo("true");
    }

    @Test
    void compare_1Gallon_3_78541Litre_shouldBeEqual() {
        QuantityMeasurementDTO result = service.compare(new QuantityInputDTO(
            new QuantityDTO(1.0, "GALLON", "VOLUME"),
            new QuantityDTO(3.78541, "LITRE", "VOLUME")
        ));
        assertThat(result.getResultString()).isEqualTo("true");
    }

    @Test
    void compare_212Fahrenheit_100Celsius_shouldBeEqual() {
        QuantityMeasurementDTO result = service.compare(new QuantityInputDTO(
            new QuantityDTO(212.0, "FAHRENHEIT", "TEMPERATURE"),
            new QuantityDTO(100.0, "CELSIUS", "TEMPERATURE")
        ));
        assertThat(result.getResultString()).isEqualTo("true");
    }

    @Test
    void add_1Feet_12Inch_shouldReturn24Inch() {
        QuantityMeasurementDTO result = service.add(new QuantityInputDTO(
            new QuantityDTO(1.0, "FEET", "LENGTH"),
            new QuantityDTO(12.0, "INCH", "LENGTH")
        ));
        assertThat(result.getResultValue()).isEqualTo(24.0);
        assertThat(result.getResultUnit()).isEqualTo("INCH");
    }

    @Test
    void add_1Gallon_3_78Litre_shouldReturn7_57Litre() {
        QuantityMeasurementDTO result = service.add(new QuantityInputDTO(
            new QuantityDTO(1.0, "GALLON", "VOLUME"),
            new QuantityDTO(3.78541, "LITRE", "VOLUME")
        ));
        assertThat(result.getResultValue()).isCloseTo(7.57082, org.assertj.core.data.Offset.offset(0.001));
    }

    @Test
    void convert_1Feet_shouldReturn12Inch() {
        QuantityMeasurementDTO result = service.convert(new QuantityInputDTO(
            new QuantityDTO(1.0, "FEET", "LENGTH"),
            new QuantityDTO(0.0, "FEET", "LENGTH")
        ));
        assertThat(result.getResultValue()).isEqualTo(12.0);
    }

    @Test
    void subtract_12Inch_from1Feet_shouldReturn0() {
        QuantityMeasurementDTO result = service.subtract(new QuantityInputDTO(
            new QuantityDTO(1.0, "FEET", "LENGTH"),
            new QuantityDTO(12.0, "INCH", "LENGTH")
        ));
        assertThat(result.getResultValue()).isCloseTo(0.0, org.assertj.core.data.Offset.offset(0.001));
    }

    // ---- REST Endpoint Integration ----

    @Test
    void compareEndpoint_returns200_withExpectedBody() throws Exception {
        String body = """
            {
              "thisQuantityDTO": { "value": 1.0, "unit": "FEET", "measurementType": "LENGTH" },
              "thatQuantityDTO": { "value": 12.0, "unit": "INCH", "measurementType": "LENGTH" }
            }
            """;

        mockMvc.perform(post("/api/v1/quantities/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultString").value("true"))
            .andExpect(jsonPath("$.operation").value("COMPARE"))
            .andExpect(jsonPath("$.error").value(false));
    }

    @Test
    void addEndpoint_returns200_withSum() throws Exception {
        String body = """
            {
              "thisQuantityDTO": { "value": 2.0, "unit": "INCH", "measurementType": "LENGTH" },
              "thatQuantityDTO": { "value": 2.0, "unit": "INCH", "measurementType": "LENGTH" }
            }
            """;

        mockMvc.perform(post("/api/v1/quantities/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultValue").value(4.0));
    }

    @Test
    void compareEndpoint_invalidUnit_returns400() throws Exception {
        String body = """
            {
              "thisQuantityDTO": { "value": 1.0, "unit": "PARSEC", "measurementType": "LENGTH" },
              "thatQuantityDTO": { "value": 1.0, "unit": "INCH",   "measurementType": "LENGTH" }
            }
            """;

        mockMvc.perform(post("/api/v1/quantities/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void historyByOperationEndpoint_returns200() throws Exception {
        // Seed a record first
        service.compare(new QuantityInputDTO(
            new QuantityDTO(1.0, "INCH", "LENGTH"),
            new QuantityDTO(1.0, "INCH", "LENGTH")
        ));

        mockMvc.perform(get("/api/v1/quantities/history/operation/COMPARE"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].operation").value("COMPARE"));
    }

    @Test
    void errorHistoryEndpoint_returns200() throws Exception {
        mockMvc.perform(get("/api/v1/quantities/history/errored"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void countEndpoint_returnsCorrectCount() throws Exception {
        service.add(new QuantityInputDTO(
            new QuantityDTO(1.0, "INCH", "LENGTH"),
            new QuantityDTO(1.0, "INCH", "LENGTH")
        ));
        service.add(new QuantityInputDTO(
            new QuantityDTO(2.0, "INCH", "LENGTH"),
            new QuantityDTO(3.0, "INCH", "LENGTH")
        ));

        mockMvc.perform(get("/api/v1/quantities/count/ADD"))
            .andExpect(status().isOk())
            .andExpect(content().string("2"));
    }
}
