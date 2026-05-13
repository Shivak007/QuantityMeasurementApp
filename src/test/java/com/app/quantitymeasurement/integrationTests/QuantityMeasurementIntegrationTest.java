package com.app.quantitymeasurement.integrationTests;

import com.app.quantitymeasurement.QuantityMeasurementApp;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.app.quantitymeasurement.util.ApplicationConfig;
import com.app.quantitymeasurement.util.ConnectionPool;
import org.junit.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;

/**
 * End-to-end integration tests verifying all layers work together
 * with a real H2 database and connection pool.
 *
 * UC16: Integration Tests
 */
public class QuantityMeasurementIntegrationTest {

    private static ConnectionPool pool;
    private QuantityMeasurementApp app;

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.setProperty("db.driver",   "org.h2.Driver");
        System.setProperty("db.url",
            "jdbc:h2:mem:integrationdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        System.setProperty("db.username", "sa");
        System.setProperty("db.password", "");
        System.setProperty("db.pool.initialSize", "3");
        System.setProperty("db.pool.maxSize",     "5");

        ApplicationConfig.reset();
        pool = ConnectionPool.createPool(ApplicationConfig.getInstance());

        try (Connection conn = pool.acquireConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS quantity_measurement_history");
            stmt.execute("DROP TABLE IF EXISTS quantity_measurement_entity");
            stmt.execute(
                "CREATE TABLE quantity_measurement_entity (" +
                "  id               BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "  operation_type   VARCHAR(50)  NOT NULL," +
                "  measurement_type VARCHAR(50)  NOT NULL," +
                "  first_value      DOUBLE       NOT NULL," +
                "  first_unit       VARCHAR(50)  NOT NULL," +
                "  second_value     DOUBLE," +
                "  second_unit      VARCHAR(50)," +
                "  result           DOUBLE," +
                "  result_boolean   BOOLEAN," +
                "  created_at       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
            pool.releaseConnection(conn);
        }
    }

    @Before
    public void setUp() {
        QuantityMeasurementDatabaseRepository repo =
            new QuantityMeasurementDatabaseRepository(pool);
        repo.deleteAll();
        app = new QuantityMeasurementApp(repo);
    }

    @AfterClass
    public static void tearDownClass() {
        if (pool != null) pool.shutdown();
        ApplicationConfig.reset();
    }

    // ---- Length Comparisons ----

    @Test
    public void testIntegration_Compare_1Feet_12Inch_Equal() {
        boolean result = app.compare(1.0, "FEET", 12.0, "INCH", "LENGTH");
        assertTrue("1 FEET should equal 12 INCH", result);
        assertEquals(1, app.getTotalCount());
    }

    @Test
    public void testIntegration_Compare_1Yard_3Feet_Equal() {
        boolean result = app.compare(1.0, "YARD", 3.0, "FEET", "LENGTH");
        assertTrue("1 YARD should equal 3 FEET", result);
    }

    @Test
    public void testIntegration_Compare_1Feet_1Inch_NotEqual() {
        boolean result = app.compare(1.0, "FEET", 1.0, "INCH", "LENGTH");
        assertFalse("1 FEET should NOT equal 1 INCH", result);
    }

    // ---- Weight Comparisons ----

    @Test
    public void testIntegration_Compare_1Kilogram_1000Gram_Equal() {
        boolean result = app.compare(1.0, "KILOGRAM", 1000.0, "GRAM", "WEIGHT");
        assertTrue("1 KILOGRAM should equal 1000 GRAM", result);
    }

    @Test
    public void testIntegration_Compare_1Tonne_1000Kilogram_Equal() {
        boolean result = app.compare(1.0, "TONNE", 1000.0, "KILOGRAM", "WEIGHT");
        assertTrue("1 TONNE should equal 1000 KILOGRAM", result);
    }

    // ---- Volume ----

    @Test
    public void testIntegration_Compare_1Gallon_3_78Litre_Equal() {
        boolean result = app.compare(1.0, "GALLON", 3.78541, "LITRE", "VOLUME");
        assertTrue("1 GALLON should equal 3.78541 LITRE", result);
    }

    // ---- Temperature ----

    @Test
    public void testIntegration_Compare_212F_100C_Equal() {
        boolean result = app.compare(212.0, "FAHRENHEIT", 100.0, "CELSIUS", "TEMPERATURE");
        assertTrue("212 FAHRENHEIT should equal 100 CELSIUS", result);
    }

    // ---- Addition ----

    @Test
    public void testIntegration_Add_2Inch_2Inch_Returns4() {
        double result = app.add(2.0, "INCH", 2.0, "INCH", "LENGTH");
        assertEquals(4.0, result, 0.001);
    }

    @Test
    public void testIntegration_Add_1Feet_12Inch_Returns24() {
        double result = app.add(1.0, "FEET", 12.0, "INCH", "LENGTH");
        assertEquals(24.0, result, 0.001);
    }

    @Test
    public void testIntegration_Add_1Gallon_3_78Litre_Returns7_57() {
        double result = app.add(1.0, "GALLON", 3.78541, "LITRE", "VOLUME");
        assertEquals(7.57082, result, 0.001);
    }

    @Test
    public void testIntegration_Add_1Kg_1000Gram_Returns2000() {
        double result = app.add(1.0, "KILOGRAM", 1000.0, "GRAM", "WEIGHT");
        assertEquals(2000.0, result, 0.001);
    }

    // ---- Convert ----

    @Test
    public void testIntegration_Convert_1Feet_Returns12Inch() {
        double result = app.convert(1.0, "FEET", "LENGTH");
        assertEquals(12.0, result, 0.001);
    }

    @Test
    public void testIntegration_Convert_212F_Returns100C() {
        double result = app.convert(212.0, "FAHRENHEIT", "TEMPERATURE");
        assertEquals(100.0, result, 0.001);
    }

    // ---- Persistence across operations ----

    @Test
    public void testIntegration_MultipleOperations_AllPersisted() {
        app.compare(1.0, "FEET", 12.0, "INCH", "LENGTH");
        app.add(2.0, "INCH", 2.0, "INCH", "LENGTH");
        app.convert(1.0, "KILOGRAM", "WEIGHT");

        assertEquals(3, app.getTotalCount());
    }

    @Test
    public void testIntegration_GetAllMeasurements_ReturnsAll() {
        app.compare(1.0, "FEET", 12.0, "INCH", "LENGTH");
        app.add(1.0, "GALLON", 3.78541, "LITRE", "VOLUME");

        List<QuantityMeasurementEntity> all = app.getAllMeasurements();
        assertEquals(2, all.size());
    }

    @Test
    public void testIntegration_GetMeasurementsByOperation_FiltersCorrectly() {
        app.compare(1.0, "FEET", 12.0, "INCH", "LENGTH");
        app.compare(1.0, "KILOGRAM", 1000.0, "GRAM", "WEIGHT");
        app.add(2.0, "INCH", 2.0, "INCH", "LENGTH");

        List<QuantityMeasurementEntity> comparisons =
            app.getMeasurementsByOperation("COMPARE");
        assertEquals(2, comparisons.size());
    }

    @Test
    public void testIntegration_GetMeasurementsByType_FiltersCorrectly() {
        app.compare(1.0, "FEET", 12.0, "INCH", "LENGTH");
        app.add(1.0, "FEET", 12.0, "INCH", "LENGTH");
        app.compare(1.0, "KILOGRAM", 1000.0, "GRAM", "WEIGHT");

        List<QuantityMeasurementEntity> lengthOps = app.getMeasurementsByType("LENGTH");
        assertEquals(2, lengthOps.size());
    }

    @Test
    public void testIntegration_DeleteAllMeasurements_ClearsDatabase() {
        app.compare(1.0, "FEET", 12.0, "INCH", "LENGTH");
        app.add(2.0, "INCH", 2.0, "INCH", "LENGTH");
        assertEquals(2, app.getTotalCount());

        app.deleteAllMeasurements();
        assertEquals(0, app.getTotalCount());
    }

    @Test
    public void testIntegration_IsolationBetweenTests_DatabaseIsClean() {
        // setUp() calls deleteAll() so each test starts empty
        assertEquals("Each test should start with 0 measurements", 0, app.getTotalCount());
    }

    @Test
    public void testIntegration_PoolStatistics_NotNull() {
        String stats = app.getRepositoryStatistics();
        assertNotNull(stats);
        assertFalse(stats.isEmpty());
    }

    @Test
    public void testIntegration_RepositoryStatistics_ContainsPoolInfo() {
        String stats = app.getRepositoryStatistics();
        // H2 connection pool should report meaningful info
        assertTrue("Stats should contain 'Connection' or 'Pool' info",
            stats.toLowerCase().contains("connection") ||
            stats.toLowerCase().contains("pool") ||
            stats.toLowerCase().contains("total"));
    }
}
