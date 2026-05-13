package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.DatabaseException;
import com.app.quantitymeasurement.util.ApplicationConfig;
import com.app.quantitymeasurement.util.ConnectionPool;
import org.junit.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit / Integration tests for QuantityMeasurementDatabaseRepository
 * using H2 in-memory database.
 *
 * UC16: Database Integration with JDBC
 */
public class QuantityMeasurementDatabaseRepositoryTest {

    private static ConnectionPool pool;
    private QuantityMeasurementDatabaseRepository repository;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Use H2 in-memory DB for tests
        System.setProperty("db.driver",   "org.h2.Driver");
        System.setProperty("db.url",
            "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        System.setProperty("db.username", "sa");
        System.setProperty("db.password", "");
        System.setProperty("db.pool.initialSize", "3");
        System.setProperty("db.pool.maxSize",     "5");

        ApplicationConfig.reset();
        pool = ConnectionPool.createPool(ApplicationConfig.getInstance());

        // Create schema
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
        repository = new QuantityMeasurementDatabaseRepository(pool);
        repository.deleteAll();   // clean slate for each test
    }

    @AfterClass
    public static void tearDownClass() {
        if (pool != null) pool.shutdown();
        ApplicationConfig.reset();
    }

    // ---- SAVE ----

    @Test
    public void testSaveEntity_AssignsId() {
        QuantityMeasurementEntity entity = buildEntity("COMPARE", "LENGTH");
        QuantityMeasurementEntity saved  = repository.save(entity);
        assertTrue("Saved entity should have id > 0", saved.getId() > 0);
    }

    @Test
    public void testSaveEntity_PersistsData() {
        QuantityMeasurementEntity entity = buildEntity("ADD", "WEIGHT");
        entity.setFirstValue(1.0);
        entity.setFirstUnit("KILOGRAM");
        entity.setSecondValue(1000.0);
        entity.setSecondUnit("GRAM");
        entity.setResult(2000.0);

        repository.save(entity);

        List<QuantityMeasurementEntity> all = repository.getAllMeasurements();
        assertEquals(1, all.size());
        assertEquals("ADD",      all.get(0).getOperationType());
        assertEquals("WEIGHT",   all.get(0).getMeasurementType());
        assertEquals(2000.0,     all.get(0).getResult(), 0.001);
    }

    // ---- GET ALL ----

    @Test
    public void testGetAllMeasurements_EmptyRepository() {
        List<QuantityMeasurementEntity> all = repository.getAllMeasurements();
        assertNotNull(all);
        assertEquals(0, all.size());
    }

    @Test
    public void testGetAllMeasurements_ReturnsCorrectCount() {
        repository.save(buildEntity("COMPARE", "LENGTH"));
        repository.save(buildEntity("ADD",     "VOLUME"));
        repository.save(buildEntity("CONVERT", "TEMPERATURE"));

        assertEquals(3, repository.getAllMeasurements().size());
    }

    // ---- GET BY OPERATION ----

    @Test
    public void testGetMeasurementsByOperation_FiltersCorrectly() {
        repository.save(buildEntity("COMPARE", "LENGTH"));
        repository.save(buildEntity("COMPARE", "WEIGHT"));
        repository.save(buildEntity("ADD",     "VOLUME"));

        List<QuantityMeasurementEntity> compareOps =
            repository.getMeasurementsByOperation("COMPARE");

        assertEquals(2, compareOps.size());
        compareOps.forEach(e ->
            assertEquals("COMPARE", e.getOperationType()));
    }

    @Test
    public void testGetMeasurementsByOperation_CaseInsensitive() {
        repository.save(buildEntity("COMPARE", "LENGTH"));
        List<QuantityMeasurementEntity> result =
            repository.getMeasurementsByOperation("compare");
        assertEquals(1, result.size());
    }

    @Test
    public void testGetMeasurementsByOperation_NoMatch_ReturnsEmpty() {
        repository.save(buildEntity("COMPARE", "LENGTH"));
        List<QuantityMeasurementEntity> result =
            repository.getMeasurementsByOperation("NON_EXISTENT");
        assertTrue(result.isEmpty());
    }

    // ---- GET BY TYPE ----

    @Test
    public void testGetMeasurementsByType_FiltersCorrectly() {
        repository.save(buildEntity("COMPARE", "LENGTH"));
        repository.save(buildEntity("ADD",     "LENGTH"));
        repository.save(buildEntity("COMPARE", "WEIGHT"));

        List<QuantityMeasurementEntity> lengthMeasurements =
            repository.getMeasurementsByType("LENGTH");

        assertEquals(2, lengthMeasurements.size());
        lengthMeasurements.forEach(e ->
            assertEquals("LENGTH", e.getMeasurementType()));
    }

    @Test
    public void testGetMeasurementsByType_CaseInsensitive() {
        repository.save(buildEntity("ADD", "VOLUME"));
        List<QuantityMeasurementEntity> result =
            repository.getMeasurementsByType("volume");
        assertEquals(1, result.size());
    }

    // ---- COUNT ----

    @Test
    public void testGetTotalCount_Empty() {
        assertEquals(0, repository.getTotalCount());
    }

    @Test
    public void testGetTotalCount_AfterSaves() {
        repository.save(buildEntity("COMPARE", "LENGTH"));
        repository.save(buildEntity("ADD",     "WEIGHT"));
        assertEquals(2, repository.getTotalCount());
    }

    // ---- DELETE ALL ----

    @Test
    public void testDeleteAll_ClearsAllRecords() {
        repository.save(buildEntity("COMPARE", "LENGTH"));
        repository.save(buildEntity("ADD",     "VOLUME"));
        assertEquals(2, repository.getTotalCount());

        repository.deleteAll();

        assertEquals(0, repository.getTotalCount());
    }

    @Test
    public void testDeleteAll_OnEmptyRepository_DoesNotThrow() {
        repository.deleteAll();   // should not throw
        assertEquals(0, repository.getTotalCount());
    }

    // ---- SQL INJECTION PREVENTION ----

    @Test
    public void testSQLInjectionPrevention_OperationFilter() {
        repository.save(buildEntity("COMPARE", "LENGTH"));
        // Attempt injection; parameterized query treats it as a literal string
        List<QuantityMeasurementEntity> result =
            repository.getMeasurementsByOperation("COMPARE' OR '1'='1");
        assertTrue("Injection attempt should return 0 results", result.isEmpty());
    }

    // ---- POOL STATISTICS ----

    @Test
    public void testGetPoolStatistics_ReturnsNonNull() {
        String stats = repository.getPoolStatistics();
        assertNotNull(stats);
        assertFalse(stats.isEmpty());
    }

    // ---- TIMESTAMP ----

    @Test
    public void testTimestamp_PersistedAndRetrieved() {
        QuantityMeasurementEntity entity = buildEntity("CONVERT", "TEMPERATURE");
        repository.save(entity);

        QuantityMeasurementEntity retrieved = repository.getAllMeasurements().get(0);
        assertNotNull("createdAt should not be null", retrieved.getCreatedAt());
    }

    // ---- LARGE DATASET ----

    @Test
    public void testLargeDataSet_SavesAndRetrievesAll() {
        int count = 100;
        for (int i = 0; i < count; i++) {
            repository.save(buildEntity("ADD", "LENGTH"));
        }
        assertEquals(count, repository.getTotalCount());
        assertEquals(count, repository.getAllMeasurements().size());
    }

    // ---- Helper ----

    private QuantityMeasurementEntity buildEntity(String operationType, String measurementType) {
        return new QuantityMeasurementEntity(
            operationType, measurementType,
            1.0, "FEET",
            12.0, "INCH",
            12.0, true
        );
    }
}
