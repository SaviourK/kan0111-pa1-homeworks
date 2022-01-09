package com.kanok.n_queen;

public class NQueenApp {

    private static final int NUMBER_OF_CORES = 8;
    private static final int N = 15;

    public static void main(String[] args) {
        NQueen nQueen = new NQueen(NUMBER_OF_CORES, N);
        nQueen.start();
    }
}
