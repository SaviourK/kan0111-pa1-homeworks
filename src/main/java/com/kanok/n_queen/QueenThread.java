package com.kanok.n_queen;

public class QueenThread extends Thread {

    private final int nMinusOne;
    private int level;
    private int seed;
    private final int[] s;
    private long solutions;
    private final Seed server;

    public QueenThread(int seed, int n, Seed server) {
        this.nMinusOne = n - 1;
        this.s = new int[n];
        this.level = 1;
        for (int i = 0; i < n; i++) this.s[i] = -1;
        this.seed = seed;
        this.server = server;
    }

    @Override
    public void run() {
        do {

            this.s[0] = this.seed;
            this.level = 1;

            do {
                generate();
                if (solution()) {
                    this.solutions++;
                }
                if (criterion()) {
                    level++;
                } else {
                    while (level > 0 && !moreBrothers()) {
                        back();
                    }
                }
            } while (level > 0);

            this.seed = this.server.getNextSeed();

        } while (this.seed != -1);

    }

    private void generate() {
        s[level]++;
    }

    private boolean solution() {
        if (level != nMinusOne) {
            return false;
        }
        return solutionOrCriterion(nMinusOne);
    }

    private boolean criterion() {
        if (level == nMinusOne) {
            return false;
        }
        return solutionOrCriterion(level);
    }

    private boolean solutionOrCriterion(int nMinusOne) {
        for (int i = 0; i < nMinusOne; i++) {
            if (s[i] == s[nMinusOne]) {
                return false;
            }
            if ((s[i] - s[nMinusOne]) == (nMinusOne - i)) {
                return false;
            }
            if ((s[nMinusOne] - s[i]) == (nMinusOne - i)) {
                return false;
            }
        }
        return true;
    }

    private boolean moreBrothers() {
        return s[level] < nMinusOne;
    }

    private void back() {
        s[level] = -1;
        level--;
    }

    public long getSolutions() {
        return this.solutions;
    }
}
