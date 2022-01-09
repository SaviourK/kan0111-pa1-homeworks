package com.kanok.k_means;

import com.kanok.k_means.calculator.ParallelKMeansCalculator;
import com.kanok.k_means.calculator.SerialKMeansCalculator;
import com.kanok.k_means.model.Point2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class KMeans {

    private static final int NUMBER_OF_THREADS = 8;
    private static final int K = 20;
    private static final int REPLICATION_FACTOR = 1000;
    private static final String RESOURCE_FILE_NAME = "/k_means/k_means.data";
    private static final Utils utils = new Utils();

    private static final Logger logger = LoggerFactory.getLogger(KMeans.class);

    public void start() {
        logger.info("START K-MEANS");

        try {
            List<Point2D> dataset = utils.createDataSet(RESOURCE_FILE_NAME, REPLICATION_FACTOR);
            logger.info("Data set size {}", dataset.size());
            List<Point2D> centers = utils.initializeRandomCenters(K, 0, dataset.size());

            serialKMeansCalculate(dataset, centers);

            parallelKMeansCalculate(dataset, centers);
        } catch (Exception e) {
            logger.error("Cannot read data from resource file {}", RESOURCE_FILE_NAME);
        }

        logger.info("END K-MEANS");
        System.exit(0);
    }

    private void serialKMeansCalculate(List<Point2D> dataset, List<Point2D> centers) {
        logger.info("Starting serial K-Means calculator");
        SerialKMeansCalculator serialKMeansCalculator = new SerialKMeansCalculator();
        long start = System.currentTimeMillis();
        final List<Point2D> bestCenters = serialKMeansCalculator.serialKMeans(centers, dataset);
        long end = System.currentTimeMillis();
        logger.info("Serial K-Means best centers {}", bestCenters);
        logger.info("End of serial K-Means calculator. Time elapsed: {}ms", end - start);
    }

    private void parallelKMeansCalculate(List<Point2D> dataset, List<Point2D> centers) {
        logger.info("Starting parallel K-Means calculator. Number of threads {}", NUMBER_OF_THREADS);
        ParallelKMeansCalculator parallelKMeansCalculator = new ParallelKMeansCalculator(NUMBER_OF_THREADS);
        long start = System.currentTimeMillis();
        final List<Point2D> bestCenters = parallelKMeansCalculator.parallelKMeans(centers, dataset);
        long end = System.currentTimeMillis();
        logger.info("Serial K-Means best centers {}", bestCenters);
        logger.info("End of serial K-Means calculator. Time elapsed: {}ms", end - start);
    }

}
