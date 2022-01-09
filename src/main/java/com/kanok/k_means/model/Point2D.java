package com.kanok.k_means.model;

import java.util.List;
import java.util.Objects;

public class Point2D {

    private final float x;
    private final float y;

    public Point2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public int getNearestPointIndex(List<Point2D> points) {
        int index = -1;
        double minDist = Double.MAX_VALUE;
        for (int i = 0; i < points.size(); i++) {
            double dist = calcDistance(points.get(i));
            if (dist < minDist) {
                minDist = dist;
                index = i;
            }
        }
        return index;
    }

    public static Point2D getMean(List<Point2D> points) {
        float accumX = 0;
        float accumY = 0;
        if (points.isEmpty()) {
            return new Point2D(accumX, accumY);
        }
        for (Point2D p : points) {
            accumX += p.x;
            accumY += p.y;
        }
        final int pointSize = points.size();
        return new Point2D(accumX / pointSize, accumY / pointSize);
    }

    public double calcDistance(Point2D secondPoint) {
        return Math.sqrt(Math.pow(this.x - secondPoint.x, 2)
                + Math.pow(this.y - secondPoint.y, 2));
    }

    @Override
    public String toString() {
        return "Point2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point2D point2D = (Point2D) o;
        return Float.compare(point2D.x, x) == 0 && Float.compare(point2D.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
