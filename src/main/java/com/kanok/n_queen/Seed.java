package com.kanok.n_queen;

public class Seed {

    private final int n;
    private int usedSeeds;

    public Seed(int n, int cores) {
        this.n = n;
        this.usedSeeds = cores;
    }

    public synchronized int getNextSeed() {
        if (this.n % 2 == 0 && this.usedSeeds < this.n / 2 || this.n % 2 == 1 && this.usedSeeds < this.n / 2 + 1) {
            this.usedSeeds++;
            return this.usedSeeds - 1;
        }
        return -1;
    }
}
