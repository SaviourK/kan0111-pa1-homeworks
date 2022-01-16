package com.kanok.cellular;

public class CellularThread implements Runnable {

    private final int r;
    private final boolean[][] newBoard;
    private final int size;
    private final boolean[][] life;
    private final Object locker;

    public CellularThread(int r, boolean[][] newBoard, int size, boolean[][] life) {
        this.locker = new Object();
        this.r = r;
        this.newBoard = newBoard;
        this.size = size;
        this.life = life;
    }

    @Override
    public void run() {
        int above = r > 0 ? r - 1 : size - 1;
        int below = r < size - 1 ? r + 1 : 0;

        for (int c = 0; c < size; c++) {
            int n = getN(above, below, c);
            if (n == 3 || (life[r][c] && n == 2))
                synchronized (locker) {
                    newBoard[r][c] = true;
                }
            else
                synchronized (locker) {
                    newBoard[r][c] = false;
                }
        }
    }

    private int getN(int above, int below, int c) {
        int left = c > 0 ? c - 1 : size - 1;
        int right = c < size - 1 ? c + 1 : 0;
        int n = 0;

        if (life[above][left]) {
            n++;
        }
        if (life[above][c]) {
            n++;
        }
        if (life[above][right]) {
            n++;
        }
        if (life[r][left]) {
            n++;
        }
        if (life[r][right]) {
            n++;
        }
        if (life[below][left]) {
            n++;
        }
        if (life[below][c]) {
            n++;
        }
        if (life[below][right]) {
            n++;
        }

        return n;
    }
}
