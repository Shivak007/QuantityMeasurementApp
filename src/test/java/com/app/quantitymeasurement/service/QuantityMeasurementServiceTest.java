package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for QuantityMeasurementServiceImpl using Mockito.
 * The repository is mocked so no database is needed.
 *
 * UC16: Service Layer Unit Tests
 */
public class QuantityMeasurementServiceTest {

    private IQuantityMeasurementRepository mockRepository;
    private QuantityMeasurementServiceImpl service;

    @Before
    public void setUp() {
        mockRepository = mock(IQuantityMeasurementRepository.class);
        // save() returns the entity passed to it
        when(mockRepository.save(any(QuantityMeasurementEntity.class)))
            .thenAnswer(inv -> inv.getArgument(0));
        service = new QuantityMeasurementServiceImpl(mockRepository);
    }

    // ---- COMPARE - LENGTH ----

    @Test
    public void testCompare_1Feet_12Inch_ShouldBeEqual() {
        QuantityDTO dto = new QuantityDTO(1.0, "FEET", 12.0, "INCH", "COMPARE", "LENGTH");
        assertTrue(service.compare(dto));
    }

    @Test
    public void testCompare_1Yard_3Feet_ShouldBeEqual() {
        QuantityDTO dto = new QuantityDTO(1.0, "YARD", 3.0, "FEET", "COMPARE", "LENGTH");
        assertTrue(service.compare(dto));
    }

    @Test
    public void testCompare_2Inch_2Inch_ShouldBeEqual() {
        QuantityDTO dto = new QuantityDTO(2.0, "INCH", 2.0, "INCH", "COMPARE", "LENGTH");
        assertTrue(service.compare(dto));
    }

    @Test
    public void testCompare_1Feet_1Inch_ShouldNotBeEqual() {
        QuantityDTO dto = new QuantityDTO(1.0, "FEET", 1.0, "INCH", "COMPARE", "LENGTH");
        assertFalse(service.compare(dto));
    }

    // ---- COMPARE - WEIGHT ----

    @Test
    public void testCompare_1Kilogram_1000Gram_ShouldBeEqual() {
        QuantityDTO dto = new QuantityDTO(1.0, "KILOGRAM", 1000.0, "GRAM", "COMPARE", "WEIGHT");
        assertTrue(service.compare(dto));
    }

    @Test
    public void testCompare_1Tonne_1000Kilogram_ShouldBeEqual() {
        QuantityDTO dto = new QuantityDTO(1.0, "TONNE", 1000.0, "KILOGRAM", "COMPARE", "WEIGHT");
        assertTrue(service.compare(dto));
    }

    // ---- COMPARE - VOLUME ----

    @Test
    public void testCompare_1Gallon_3_78Litre_ShouldBeEqual() {
        QuantityDTO dto = new QuantityDTO(1.0, "GALLON", 3.78541, "LITRE", "COMPARE", "VOLUME");
        assertTrue(service.compare(dto));
    }

    // ---- COMPARE - TEMPERATURE ----

    @Test
    public void testCompare_212Fahrenheit_100Celsius_ShouldBeEqual() {
        // 212F = 100C
        QuantityDTO dto = new QuantityDTO(212.0, "FAHRENHEIT", 100.0, "CELSIUS", "COMPARE", "TEMPERATURE");
        assertTrue(service.compare(dto));
    }

    // ---- ADD ----

    @Test
    public void testAdd_2Inch_2Inch_ShouldReturn4Inch() {
        QuantityDTO dto = new QuantityDTO(2.0, "INCH", 2.0, "INCH", "ADD", "LENGTH");
        assertEquals(4.0, service.add(dto), 0.001);
    }

    @Test
    public void testAdd_1Feet_12Inch_ShouldReturn24Inch() {
        QuantityDTO dto = new QuantityDTO(1.0, "FEET", 12.0, "INCH", "ADD", "LENGTH");
        assertEquals(24.0, service.add(dto), 0.001);
    }

    @Test
    public void testAdd_1Gallon_3_78Litre_ShouldReturn7_57Litre() {
        QuantityDTO dto = new QuantityDTO(1.0, "GALLON", 3.78541, "LITRE", "ADD", "VOLUME");
        assertEquals(7.57082, service.add(dto), 0.001);
    }

    @Test
    public void testAdd_1Kg_1000Gram_ShouldReturn2000Gram() {
        QuantityDTO dto = new QuantityDTO(1.0, "KILOGRAM", 1000.0, "GRAM", "ADD", "WEIGHT");
        assertEquals(2000.0, service.add(dto), 0.001);
    }

    // ---- CONVERT ----

    @Test
    public void testConvert_1Feet_ShouldReturn12Inch() {
        QuantityDTO dto = new QuantityDTO(1.0, "FEET", 0, "FEET", "CONVERT", "LENGTH");
        assertEquals(12.0, service.convert(dto), 0.001);
    }

    @Test
    public void testConvert_212Fahrenheit_ShouldReturn100Celsius() {
        QuantityDTO dto = new QuantityDTO(212.0, "FAHRENHEIT", 0, "FAHRENHEIT", "CONVERT", "TEMPERATURE");
        assertEquals(100.0, service.convert(dto), 0.001);
    }

    @Test
    public void testConvert_0Celsius_ShouldReturn0() {
        QuantityDTO dto = new QuantityDTO(0.0, "CELSIUS", 0, "CELSIUS", "CONVERT", "TEMPERATURE");
        assertEquals(0.0, service.convert(dto), 0.001);
    }

    // ---- PERSISTENCE VERIFICATION ----

    @Test
    public void testCompare_PersistsEntityToRepository() {
        QuantityDTO dto = new QuantityDTO(1.0, "FEET", 12.0, "INCH", "COMPARE", "LENGTH");
        service.compare(dto);

        ArgumentCaptor<QuantityMeasurementEntity> captor =
            ArgumentCaptor.forClass(QuantityMeasurementEntity.class);
        verify(mockRepository, times(1)).save(captor.capture());

        QuantityMeasurementEntity saved = captor.getValue();
        assertEquals("COMPARE", saved.getOperationType());
        assertEquals("LENGTH",  saved.getMeasurementType());
        assertEquals(1.0,       saved.getFirstValue(),  0.001);
        assertEquals("FEET",    saved.getFirstUnit());
        assertEquals(12.0,      saved.getSecondValue(), 0.001);
        assertEquals("INCH",    saved.getSecondUnit());
        assertTrue(saved.isResultBoolean());
    }

    @Test
    public void testAdd_PersistsEntityToRepository() {
        QuantityDTO dto = new QuantityDTO(2.0, "INCH", 2.0, "INCH", "ADD", "LENGTH");
        service.add(dto);

        ArgumentCaptor<QuantityMeasurementEntity> captor =
            ArgumentCaptor.forClass(QuantityMeasurementEntity.class);
        verify(mockRepository, times(1)).save(captor.capture());
        assertEquals(4.0, captor.getValue().getResult(), 0.001);
    }

    // ---- DELEGATION TESTS ----

    @Test
    public void testGetAllMeasurements_DelegatesToRepository() {
        List<QuantityMeasurementEntity> expected =
            Arrays.asList(new QuantityMeasurementEntity(), new QuantityMeasurementEntity());
        when(mockRepository.getAllMeasurements()).thenReturn(expected);

        List<QuantityMeasurementEntity> actual = service.getAllMeasurements();
        assertEquals(2, actual.size());
        verify(mockRepository).getAllMeasurements();
    }

    @Test
    public void testGetTotalCount_DelegatesToRepository() {
        when(mockRepository.getTotalCount()).thenReturn(5);
        assertEquals(5, service.getTotalCount());
        verify(mockRepository).getTotalCount();
    }

    @Test
    public void testDeleteAllMeasurements_DelegatesToRepository() {
        service.deleteAllMeasurements();
        verify(mockRepository, times(1)).deleteAll();
    }

    @Test
    public void testGetMeasurementsByOperation_DelegatesToRepository() {
        when(mockRepository.getMeasurementsByOperation("COMPARE"))
            .thenReturn(Collections.emptyList());
        service.getMeasurementsByOperation("COMPARE");
        verify(mockRepository).getMeasurementsByOperation("COMPARE");
    }

    @Test
    public void testGetMeasurementsByType_DelegatesToRepository() {
        when(mockRepository.getMeasurementsByType("LENGTH"))
            .thenReturn(Collections.emptyList());
        service.getMeasurementsByType("LENGTH");
        verify(mockRepository).getMeasurementsByType("LENGTH");
    }

    @Test
    public void testGetRepositoryStatistics_DelegatesToRepository() {
        when(mockRepository.getPoolStatistics()).thenReturn("stats");
        assertEquals("stats", service.getRepositoryStatistics());
    }
}
