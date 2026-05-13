package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for QuantityMeasurementController using Mockito.
 * The service layer is mocked to isolate controller behaviour.
 *
 * UC16: Controller Layer Unit Tests
 */
public class QuantityMeasurementControllerTest {

    private IQuantityMeasurementService mockService;
    private QuantityMeasurementController controller;

    @Before
    public void setUp() {
        mockService = mock(IQuantityMeasurementService.class);
        controller  = new QuantityMeasurementController(mockService);
    }

    // ---- COMPARE ----

    @Test
    public void testCompare_DelegatesToService_AndReturnsResult() {
        when(mockService.compare(any())).thenReturn(true);
        boolean result = controller.compare(1.0, "FEET", 12.0, "INCH", "LENGTH");
        assertTrue(result);
        verify(mockService, times(1)).compare(any());
    }

    @Test
    public void testCompare_ReturnsFalseWhenServiceReturnsFalse() {
        when(mockService.compare(any())).thenReturn(false);
        boolean result = controller.compare(1.0, "FEET", 1.0, "INCH", "LENGTH");
        assertFalse(result);
    }

    // ---- ADD ----

    @Test
    public void testAdd_DelegatesToService_AndReturnsResult() {
        when(mockService.add(any())).thenReturn(24.0);
        double result = controller.add(1.0, "FEET", 12.0, "INCH", "LENGTH");
        assertEquals(24.0, result, 0.001);
        verify(mockService, times(1)).add(any());
    }

    // ---- CONVERT ----

    @Test
    public void testConvert_DelegatesToService_AndReturnsResult() {
        when(mockService.convert(any())).thenReturn(12.0);
        double result = controller.convert(1.0, "FEET", "LENGTH");
        assertEquals(12.0, result, 0.001);
        verify(mockService, times(1)).convert(any());
    }

    // ---- QUERY METHODS ----

    @Test
    public void testGetAllMeasurements_DelegatesToService() {
        List<QuantityMeasurementEntity> entities =
            Arrays.asList(new QuantityMeasurementEntity(), new QuantityMeasurementEntity());
        when(mockService.getAllMeasurements()).thenReturn(entities);

        List<QuantityMeasurementEntity> result = controller.getAllMeasurements();
        assertEquals(2, result.size());
        verify(mockService).getAllMeasurements();
    }

    @Test
    public void testGetMeasurementsByOperation_DelegatesToService() {
        when(mockService.getMeasurementsByOperation("COMPARE"))
            .thenReturn(Arrays.asList(new QuantityMeasurementEntity()));
        List<QuantityMeasurementEntity> result =
            controller.getMeasurementsByOperation("COMPARE");
        assertEquals(1, result.size());
        verify(mockService).getMeasurementsByOperation("COMPARE");
    }

    @Test
    public void testGetMeasurementsByType_DelegatesToService() {
        when(mockService.getMeasurementsByType("LENGTH"))
            .thenReturn(Arrays.asList(new QuantityMeasurementEntity()));
        List<QuantityMeasurementEntity> result =
            controller.getMeasurementsByType("LENGTH");
        assertEquals(1, result.size());
        verify(mockService).getMeasurementsByType("LENGTH");
    }

    @Test
    public void testGetTotalCount_DelegatesToService() {
        when(mockService.getTotalCount()).thenReturn(7);
        assertEquals(7, controller.getTotalCount());
        verify(mockService).getTotalCount();
    }

    @Test
    public void testDeleteAllMeasurements_DelegatesToService() {
        controller.deleteAllMeasurements();
        verify(mockService, times(1)).deleteAllMeasurements();
    }

    @Test
    public void testGetRepositoryStatistics_DelegatesToService() {
        when(mockService.getRepositoryStatistics()).thenReturn("pool stats");
        assertEquals("pool stats", controller.getRepositoryStatistics());
    }

    // ---- DTO CONSTRUCTION VERIFICATION ----

    @Test
    public void testCompare_BuildsCorrectDto() {
        when(mockService.compare(argThat(dto ->
            dto.getOperationType().equals("COMPARE") &&
            dto.getMeasurementType().equals("WEIGHT") &&
            dto.getFirstValue() == 1.0 &&
            dto.getFirstUnit().equals("KILOGRAM")
        ))).thenReturn(true);

        boolean result = controller.compare(1.0, "KILOGRAM", 1000.0, "GRAM", "WEIGHT");
        assertTrue(result);
    }

    @Test
    public void testAdd_BuildsCorrectDto() {
        when(mockService.add(argThat(dto ->
            dto.getOperationType().equals("ADD") &&
            dto.getMeasurementType().equals("VOLUME")
        ))).thenReturn(7.57082);

        double result = controller.add(1.0, "GALLON", 3.78541, "LITRE", "VOLUME");
        assertEquals(7.57082, result, 0.001);
    }

    @Test
    public void testConvert_BuildsCorrectDto() {
        when(mockService.convert(argThat(dto ->
            dto.getOperationType().equals("CONVERT") &&
            dto.getMeasurementType().equals("TEMPERATURE") &&
            dto.getFirstValue() == 212.0 &&
            dto.getFirstUnit().equals("FAHRENHEIT")
        ))).thenReturn(100.0);

        double result = controller.convert(212.0, "FAHRENHEIT", "TEMPERATURE");
        assertEquals(100.0, result, 0.001);
    }
}
