package com.app.quantitymeasurement;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.app.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import com.app.quantitymeasurement.util.ApplicationConfig;
import com.app.quantitymeasurement.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class QuantityMeasurementApp {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementApp.class);

    private final QuantityMeasurementController controller;
    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementApp() {
        ApplicationConfig config = ApplicationConfig.getInstance();
        String repoType = config.getRepositoryType();
        logger.info("QuantityMeasurementApp: Repository type = '{}'", repoType);

        if ("database".equalsIgnoreCase(repoType)) {
            ConnectionPool pool = ConnectionPool.getInstance();
            repository = new QuantityMeasurementDatabaseRepository(pool);
            logger.info("QuantityMeasurementApp: Using DATABASE repository.");
        } else {
            repository = new QuantityMeasurementCacheRepository();
            logger.info("QuantityMeasurementApp: Using CACHE repository.");
        }

        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repository);
        controller = new QuantityMeasurementController(service);
        logger.info("QuantityMeasurementApp: All layers initialised successfully.");
    }

    public QuantityMeasurementApp(IQuantityMeasurementRepository repository) {
        this.repository = repository;
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repository);
        this.controller = new QuantityMeasurementController(service);
        logger.info("QuantityMeasurementApp: Initialised with explicit repository: {}",
            repository.getClass().getSimpleName());
    }

    public boolean compare(double v1, String u1, double v2, String u2, String type) {
        return controller.compare(v1, u1, v2, u2, type);
    }

    public double add(double v1, String u1, double v2, String u2, String type) {
        return controller.add(v1, u1, v2, u2, type);
    }

    public double convert(double value, String fromUnit, String type) {
        return controller.convert(value, fromUnit, type);
    }

    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return controller.getAllMeasurements();
    }

    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String opType) {
        return controller.getMeasurementsByOperation(opType);
    }

    public List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType) {
        return controller.getMeasurementsByType(measurementType);
    }

    public int getTotalCount() {
        return controller.getTotalCount();
    }

    public void deleteAllMeasurements() {
        controller.deleteAllMeasurements();
    }

    public String getRepositoryStatistics() {
        return controller.getRepositoryStatistics();
    }

    public void closeResources() {
        repository.releaseResources();
        logger.info("QuantityMeasurementApp: Resources released.");
    }

    public static void main(String[] args) {
        logger.info("=== Quantity Measurement Application - UC16 ===");
        QuantityMeasurementApp app = new QuantityMeasurementApp();

        try {
            logger.info("--- Length Comparisons ---");
            boolean result1 = app.compare(1.0, "FEET", 12.0, "INCH", "LENGTH");
            logger.info("1 FEET == 12 INCH? {}", result1);

            boolean result2 = app.compare(1.0, "YARD", 3.0, "FEET", "LENGTH");
            logger.info("1 YARD == 3 FEET? {}", result2);

            logger.info("--- Weight Comparisons ---");
            boolean result3 = app.compare(1.0, "KILOGRAM", 1000.0, "GRAM", "WEIGHT");
            logger.info("1 KILOGRAM == 1000 GRAM? {}", result3);

            logger.info("--- Volume Addition ---");
            double addResult = app.add(1.0, "GALLON", 3.78541, "LITRE", "VOLUME");
            logger.info("1 GALLON + 3.78541 LITRE = {} LITRE (base)", addResult);

            logger.info("--- Temperature Conversion ---");
            double tempResult = app.convert(212.0, "FAHRENHEIT", "TEMPERATURE");
            logger.info("212 FAHRENHEIT = {} CELSIUS", tempResult);

            logger.info("--- Repository Statistics ---");
            logger.info("Pool stats: {}", app.getRepositoryStatistics());
            logger.info("Total measurements stored: {}", app.getTotalCount());

            logger.info("--- All Measurements ---");
            List<QuantityMeasurementEntity> all = app.getAllMeasurements();
            for (QuantityMeasurementEntity e : all) {
                logger.info("  {}", e);
            }

            logger.info("--- Measurements by Type: LENGTH ---");
            List<QuantityMeasurementEntity> byType = app.getMeasurementsByType("LENGTH");
            logger.info("  Found {} LENGTH measurements.", byType.size());

            logger.info("--- Measurements by Operation: COMPARE ---");
            List<QuantityMeasurementEntity> byOp = app.getMeasurementsByOperation("COMPARE");
            logger.info("  Found {} COMPARE measurements.", byOp.size());

            logger.info("--- Deleting All Measurements ---");
            app.deleteAllMeasurements();
            logger.info("Measurements after delete: {}", app.getTotalCount());

        } finally {
            app.closeResources();
            logger.info("=== Application Shutdown Complete ===");
        }
    }
}
