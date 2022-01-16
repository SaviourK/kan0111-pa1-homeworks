package com.kanok.cellular;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CellularGamePanel extends JPanel {

    private final int rows;
    private final int cols;
    private final Color bgColor;
    private final Color[][] grid;
    private boolean redraw;
    private BufferedImage image;
    private Graphics graphics;
    private boolean redrawWait;

    public CellularGamePanel(int rows, int cols, int blockWidth, int blockHeight) {
        this.rows = rows;
        this.cols = cols;
        grid = new Color[rows][cols];
        if (blockWidth > 0 && blockHeight > 0)
            setPreferredSize(new Dimension(blockWidth * cols, blockHeight * rows));
        bgColor = Color.WHITE;
        setBackground(bgColor);
        redraw = true;
    }

    public void fill(Color c) {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                grid[i][j] = c;
        forceRedraw();
    }

    public void clean() {
        fill(null);
    }

    public void changeColor(int row, int col, Color c) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            grid[row][col] = c;
            draw(row, col, true);
        }
    }

    public final synchronized void forceRedraw() {
        redrawWait = true;
        repaint();
    }

    public synchronized void autoRedraw(boolean redraw) {
        if (this.redraw == redraw)
            return;
        this.redraw = redraw;
        if (redraw)
            forceRedraw();
    }

    private synchronized void draw(int row, int col, boolean doRedraw) {
        if (doRedraw && !redraw) {
            return;
        }

        Insets insets = getInsets();
        int y = insets.top + (int) Math.round(((double) (getHeight() - insets.left - insets.right) / rows) * row);
        int h = Math.max(1, (int) Math.round(((double) (getHeight() - insets.left - insets.right) / rows) * (row + 1)) + insets.top - y);
        int x = insets.left + (int) Math.round(((double) (getWidth() - insets.top - insets.bottom) / cols) * col);
        int w = Math.max(1, (int) Math.round(((double) (getWidth() - insets.top - insets.bottom) / cols) * (col + 1)) + insets.left - x);

        Color c = grid[row][col];
        graphics.setColor((c == null) ? bgColor : c);

        if (c == null) {
            graphics.fillRect(x, y, w, h);
        } else {
            graphics.fill3DRect(x + 1, y + 1, w - 2, h - 2, true);
            graphics.drawRect(x, y, w - 1, h - 1);
        }

        if (doRedraw) {
            repaint(x, y, w, h);
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        synchronized (this) {
            if ((image == null) || image.getWidth() != getWidth() || image.getHeight() != getHeight()) {
                image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
                graphics = image.getGraphics();
                redrawWait = true;
            }
        }
        if (redrawWait) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    draw(r, c, false);
                }

            }
            redrawWait = false;
        }
        g.drawImage(image, 0, 0, null);
    }
}
