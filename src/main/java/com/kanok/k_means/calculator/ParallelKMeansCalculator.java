package com.kanok.k_means.calculator;

import com.kanok.k_means.model.Point2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelKMeansCalculator {

    private static final Logger logger = LoggerFactory.getLogger(ParallelKMeansCalculator.class);
    private final int numberOfThreads;

    public ParallelKMeansCalculator(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public List<Point2D> parallelKMeans(List<Point2D> centers, List<Point2D> dataset) {
        boolean converged;
        do {
            List<Point2D> newCenters = concurrentGetNewCenters(dataset, centers);
            double dist = getDistance(centers, newCenters);
            centers = newCenters;
            converged = dist == 0;
        } while (!converged);
        return centers;
    }

    private List<Point2D> concurrentGetNewCenters(final List<Point2D> dataset, final List<Point2D> centers) {
        final List<List<Point2D>> clusters = new ArrayList<>(centers.size());
        for (int i = 0; i < centers.size(); i++) {
            clusters.add(new ArrayList<>());
        }
        List<List<Point2D>> partitionedDataset = partition(dataset, numberOfThreads);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Callable<Void>> workers = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            workers.add(createWorker(partitionedDataset.get(i), centers, clusters));
        }
        try {
            executor.invokeAll(workers);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            System.exit(-1);
        }
        List<Point2D> newCenters = new ArrayList<>(centers.size());
        for (List<Point2D> cluster : clusters) {
            newCenters.add(Point2D.getMean(cluster));
        }
        return newCenters;
    }

    private Callable<Void> createWorker(final List<Point2D> partition, final List<Point2D> centers,
                                        final List<List<Point2D>> clusters) {
        return () -> {
            int[] indexes = new int[partition.size()];
            for (int i = 0; i < partition.size(); i++) {
                Point2D data = partition.get(i);
                int index = data.getNearestPointIndex(centers);
                indexes[i] = index;
            }
            synchronized (clusters) {
                for (int i = 0; i < indexes.length; i++) {
                    clusters.get(indexes[i]).add(partition.get(i));
                }
            }
            return null;
        };
    }

    private <V> List<List<V>> partition(List<V> list, int parts) {
        List<List<V>> lists = new ArrayList<>(parts);
        for (int i = 0; i < parts; i++) {
            lists.add(new ArrayList<>());
        }
        for (int i = 0; i < list.size(); i++) {
            lists.get(i % parts).add(list.get(i));
        }
        return lists;
    }

    private double getDistance(List<Point2D> oldCenters, List<Point2D> newCenters) {
        double accumDist = 0;
        for (int i = 0; i < oldCenters.size(); i++) {
            double dist = oldCenters.get(i).calcDistance(newCenters.get(i));
            accumDist += dist;
        }
        return accumDist;
    }
}
