package com.kanok.k_means;

import com.kanok.k_means.model.Point2D;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utils {

    public List<Point2D> createDataSet(String resourceFileName, int replicatorFactor) throws IOException {
        List<Point2D> dataset = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(this.getClass().getResourceAsStream(resourceFileName))));
        String line;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split(",");
            float x = Float.parseFloat(tokens[0]);
            float y = Float.parseFloat(tokens[1]);
            Point2D point = new Point2D(x, y);
            for (int i = 0; i < replicatorFactor; i++) {
                dataset.add(point);
            }
        }
        br.close();
        return dataset;
    }

    public List<Point2D> initializeRandomCenters(int n, int lowerBound, int upperBound) {
        List<Point2D> centers = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            float x = (float) (Math.random() * (upperBound - lowerBound) + lowerBound);
            float y = (float) (Math.random() * (upperBound - lowerBound) + lowerBound);
            Point2D point = new Point2D(x, y);
            centers.add(point);
        }
        return centers;
    }
}
