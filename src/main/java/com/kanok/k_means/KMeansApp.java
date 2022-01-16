package com.kanok.k_means;

public class KMeansApp {

    private static final int NUMBER_OF_THREADS = 8;
    private static final int K = 20;
    private static final int REPLICATION_FACTOR = 1000;
    private static final String RESOURCE_FILE_NAME = "/k_means/k_means.ds";

    public static void main(String[] args) {
        KMeans kMeans = new KMeans(NUMBER_OF_THREADS, K, REPLICATION_FACTOR, RESOURCE_FILE_NAME);
        kMeans.start();
    }
}
