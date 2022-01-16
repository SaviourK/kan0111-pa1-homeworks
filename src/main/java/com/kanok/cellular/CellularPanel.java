package com.kanok.cellular;

import smile.util.MutableInt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class CellularPanel extends JPanel implements ActionListener {

    private static final int SIZE = 100;
    private final CellularGamePanel cellularGamePanel;
    private final JButton startButton;
    private final JButton fillButton;
    private final JButton cleanButton;
    private final Timer tick;
    private boolean[][] life;

    public CellularPanel() {
        life = new boolean[SIZE][SIZE];
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);
        cellularGamePanel = new CellularGamePanel(SIZE, SIZE, 800 / SIZE, 800 / SIZE);
        add(cellularGamePanel, BoxLayout.X_AXIS);

        JPanel menu = new JPanel();
        add(menu, BoxLayout.X_AXIS);
        menu.setBackground(Color.WHITE);
        menu.setBorder(BorderFactory.createEtchedBorder());

        startButton = new JButton("Play");
        startButton.setPreferredSize(new Dimension(80, 40));
        startButton.setBackground(Color.GRAY);
        startButton.setForeground(Color.WHITE);
        fillButton = new JButton("Fill");
        fillButton.setPreferredSize(new Dimension(80, 40));
        fillButton.setBackground(Color.GRAY);
        fillButton.setForeground(Color.WHITE);
        cleanButton = new JButton("Clean");
        cleanButton.setPreferredSize(new Dimension(80, 40));
        cleanButton.setBackground(Color.GRAY);
        cleanButton.setForeground(Color.WHITE);

        menu.add(startButton);
        menu.add(fillButton);
        menu.add(cleanButton);
        startButton.addActionListener(this);
        fillButton.addActionListener(this);
        cleanButton.addActionListener(this);

        tick = new Timer(10, this);
    }

    private void display() {
        cellularGamePanel.autoRedraw(false);
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (life[r][c])
                    cellularGamePanel.changeColor(r, c, Color.BLACK);
                else
                    cellularGamePanel.changeColor(r, c, null);
            }
        }
        cellularGamePanel.autoRedraw(true);
    }

    private void nextGeneration() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        boolean[][] board = new boolean[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            CellularThread ct = new CellularThread(r, board, SIZE, life);
            executor.execute(ct);
        }
        life = board;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object in = e.getSource();
        if (in == cleanButton) {
            life = new boolean[SIZE][SIZE];
            cellularGamePanel.clean();
        } else if (in == startButton) {
            if (tick.isRunning()) {
                tick.stop();
                cleanButton.setEnabled(true);
                fillButton.setEnabled(true);
                startButton.setText("Play");
            } else {
                tick.start();
                cleanButton.setEnabled(false);
                fillButton.setEnabled(false);
                startButton.setText("Stop");
            }
        } else if (in == fillButton) {
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++)
                    life[r][c] = (Math.random() < 0.25);
            }
            display();
        } else if (in == tick) {
            nextGeneration();
            display();
        }
    }
}
