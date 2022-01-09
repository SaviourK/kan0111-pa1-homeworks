package com.kanok.k_means.calculator;

import com.kanok.k_means.model.Point2D;

import java.util.ArrayList;
import java.util.List;

public class SerialKMeansCalculator {

    public List<Point2D> serialKMeans(List<Point2D> centers, List<Point2D> dataset) {
        boolean converged;
        do {
            List<Point2D> newCenters = getNewCenters(dataset, centers);
            double dist = getDistance(centers, newCenters);
            centers = newCenters;
            converged = dist == 0;
        } while (!converged);
        return centers;
    }

    private List<Point2D> getNewCenters(List<Point2D> dataset, List<Point2D> centers) {
        List<List<Point2D>> clusters = new ArrayList<>(centers.size());
        for (int i = 0; i < centers.size(); i++) {
            clusters.add(new ArrayList<>());
        }
        for (Point2D data : dataset) {
            int index = data.getNearestPointIndex(centers);
            clusters.get(index).add(data);
        }
        List<Point2D> newCenters = new ArrayList<>(centers.size());
        for (List<Point2D> cluster : clusters) {
            newCenters.add(Point2D.getMean(cluster));
        }
        return newCenters;
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
