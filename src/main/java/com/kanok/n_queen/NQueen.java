package com.kanok.n_queen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NQueen {

    private static final Logger logger = LoggerFactory.getLogger(NQueen.class);

    private final int numberOfCores;
    private final int n;

    public NQueen(int numberOfCores, int n) {
        this.numberOfCores = numberOfCores;
        this.n = n;
    }

    public void start() {
        if (n == 1) {
            logger.info("1");
        }
        int a = 0;
        int nMedia;
        if (n % 2 == 0) {
            nMedia = n / 2;
        } else {
            nMedia = n / 2 + 1;
        }
        long start = System.currentTimeMillis();
        Seed s = new Seed(n, numberOfCores);
        QueenThread[] threads = new QueenThread[numberOfCores];


        while (a < numberOfCores && a < nMedia) {
            QueenThread h = new QueenThread(a, n, s);
            threads[a] = h;
            h.start();
            a++;
        }

        try {
            for (int i = 0; i < a; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            logger.error("Failed joining threads");
        }

        int solutions = 0;

        if (n % 2 == 0) {
            for (int i = 0; i < a; i++) solutions += threads[i].getSolutions();
            solutions = solutions * 2;
        } else {
            for (int i = 0; i < a - 1; i++) solutions += threads[i].getSolutions();
            solutions = solutions * 2;
            solutions += threads[a - 1].getSolutions();
        }

        logger.info("[N={}][{} solutions]", n, solutions);
        long end = System.currentTimeMillis();
        logger.info("Elapsed time: {}ms", (end - start));
    }
}
