package com.kanok.k_means;

import com.kanok.k_means.calculator.ParallelKMeansCalculator;
import com.kanok.k_means.calculator.SerialKMeansCalculator;
import com.kanok.k_means.model.Point2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class KMeans {

    private final int numberOfThreads;
    private final int k;
    private final int replicationFactory;
    private final String resourceFileName;
    private final Utils utils;

    private static final Logger logger = LoggerFactory.getLogger(KMeans.class);

    public KMeans(int numberOfThreads, int k, int replicationFactory, String resourceFileName) {
        this.numberOfThreads = numberOfThreads;
        this.k = k;
        this.replicationFactory = replicationFactory;
        this.resourceFileName = resourceFileName;
        this.utils = new Utils();
    }

    public void start() {
        logger.info("START K-MEANS");

        try {
            List<Point2D> dataset = utils.createDataSet(resourceFileName, replicationFactory);
            logger.info("Data set size {}", dataset.size());
            List<Point2D> centers = utils.initializeRandomCenters(k, 0, dataset.size());

            serialKMeansCalculate(dataset, centers);

            parallelKMeansCalculate(dataset, centers);
        } catch (Exception e) {
            logger.error("Cannot read data from resource file {}", resourceFileName);
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
        logger.info("Starting parallel K-Means calculator. Number of threads {}", numberOfThreads);
        ParallelKMeansCalculator parallelKMeansCalculator = new ParallelKMeansCalculator(numberOfThreads);
        long start = System.currentTimeMillis();
        final List<Point2D> bestCenters = parallelKMeansCalculator.parallelKMeans(centers, dataset);
        long end = System.currentTimeMillis();
        logger.info("Serial K-Means best centers {}", bestCenters);
        logger.info("End of serial K-Means calculator. Time elapsed: {}ms", end - start);
    }

}
