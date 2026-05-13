package com.app.quantitymeasurement;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.exception.GlobalExceptionHandler;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * QuantityMeasurementControllerTest - UC17 @WebMvcTest slice tests.
 */
@WebMvcTest(QuantityMeasurementController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class QuantityMeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IQuantityMeasurementService service;

    private static final String BASE_URL = "/api/v1/quantities";

    // ---- Sample request bodies ----

    private String compareRequest() {
        return """
            {
              "thisQuantityDTO": { "value": 1.0, "unit": "FEET", "measurementType": "LENGTH" },
              "thatQuantityDTO": { "value": 12.0, "unit": "INCH", "measurementType": "LENGTH" }
            }
            """;
    }

    private String addRequest() {
        return """
            {
              "thisQuantityDTO": { "value": 2.0, "unit": "INCH", "measurementType": "LENGTH" },
              "thatQuantityDTO": { "value": 2.0, "unit": "INCH", "measurementType": "LENGTH" }
            }
            """;
    }

    private String invalidUnitRequest() {
        return """
            {
              "thisQuantityDTO": { "value": 1.0, "unit": "PARSEC", "measurementType": "LENGTH" },
              "thatQuantityDTO": { "value": 1.0, "unit": "INCH",   "measurementType": "LENGTH" }
            }
            """;
    }

    private String missingValueRequest() {
        return """
            {
              "thisQuantityDTO": { "unit": "FEET", "measurementType": "LENGTH" },
              "thatQuantityDTO": { "value": 12.0, "unit": "INCH", "measurementType": "LENGTH" }
            }
            """;
    }

    private QuantityMeasurementDTO sampleDTO(String operation, double resultValue, String resultString) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setId(1L);
        dto.setOperation(operation);
        dto.setResultValue(resultValue);
        dto.setResultString(resultString);
        dto.setError(false);
        return dto;
    }

    // ---- COMPARE ----

    @Test
    void compare_validInput_returns200() throws Exception {
        QuantityMeasurementDTO response = sampleDTO("COMPARE", 1.0, "true");
        when(service.compare(any())).thenReturn(response);

        mockMvc.perform(post(BASE_URL + "/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(compareRequest()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.operation").value("COMPARE"))
            .andExpect(jsonPath("$.resultString").value("true"))
            .andExpect(jsonPath("$.error").value(false));
    }

    // ---- CONVERT ----

    @Test
    void convert_validInput_returns200() throws Exception {
        QuantityMeasurementDTO response = sampleDTO("CONVERT", 12.0, "12.0 INCH");
        when(service.convert(any())).thenReturn(response);

        String body = """
            {
              "thisQuantityDTO": { "value": 1.0, "unit": "FEET", "measurementType": "LENGTH" },
              "thatQuantityDTO": { "value": 1.0, "unit": "FEET", "measurementType": "LENGTH" }
            }
            """;

        mockMvc.perform(post(BASE_URL + "/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultValue").value(12.0))
            .andExpect(jsonPath("$.resultString").value("12.0 INCH"));
    }

    // ---- ADD ----

    @Test
    void add_validInput_returns200() throws Exception {
        QuantityMeasurementDTO response = sampleDTO("ADD", 4.0, "4.0 INCH");
        when(service.add(any())).thenReturn(response);

        mockMvc.perform(post(BASE_URL + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(addRequest()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultValue").value(4.0));
    }

    // ---- SUBTRACT ----

    @Test
    void subtract_validInput_returns200() throws Exception {
        QuantityMeasurementDTO response = sampleDTO("SUBTRACT", 10.0, "10.0 INCH");
        when(service.subtract(any())).thenReturn(response);

        String body = """
            {
              "thisQuantityDTO": { "value": 1.0, "unit": "FEET", "measurementType": "LENGTH" },
              "thatQuantityDTO": { "value": 2.0, "unit": "INCH", "measurementType": "LENGTH" }
            }
            """;

        mockMvc.perform(post(BASE_URL + "/subtract")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.operation").value("SUBTRACT"));
    }

    // ---- VALIDATION FAILURES ----

    @Test
    void compare_invalidUnit_returns400() throws Exception {
        mockMvc.perform(post(BASE_URL + "/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidUnitRequest()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void compare_missingValue_returns400() throws Exception {
        mockMvc.perform(post(BASE_URL + "/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(missingValueRequest()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void compare_emptyBody_returns400() throws Exception {
        mockMvc.perform(post(BASE_URL + "/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    // ---- BUSINESS EXCEPTION ----

    @Test
    void compare_serviceThrowsBusinessException_returns400() throws Exception {
        when(service.compare(any()))
            .thenThrow(new QuantityMeasurementException("Different measurement types"));

        mockMvc.perform(post(BASE_URL + "/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(compareRequest()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Business Logic Error"))
            .andExpect(jsonPath("$.message").value("Different measurement types"));
    }

    // ---- HISTORY ENDPOINTS ----

    @Test
    void getHistoryByOperation_returns200() throws Exception {
        when(service.getHistoryByOperation("COMPARE")).thenReturn(List.of(sampleDTO("COMPARE", 1.0, "true")));

        mockMvc.perform(get(BASE_URL + "/history/operation/COMPARE"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].operation").value("COMPARE"));
    }

    @Test
    void getHistoryByType_returns200() throws Exception {
        when(service.getHistoryByType("LENGTH")).thenReturn(List.of(sampleDTO("ADD", 4.0, "4.0 INCH")));

        mockMvc.perform(get(BASE_URL + "/history/type/LENGTH"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getErrorHistory_returns200() throws Exception {
        when(service.getErrorHistory()).thenReturn(List.of());

        mockMvc.perform(get(BASE_URL + "/history/errored"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void countByOperation_returns200() throws Exception {
        when(service.countByOperation("ADD")).thenReturn(5L);

        mockMvc.perform(get(BASE_URL + "/count/ADD"))
            .andExpect(status().isOk())
            .andExpect(content().string("5"));
    }
}
