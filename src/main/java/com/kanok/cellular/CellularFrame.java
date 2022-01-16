package com.kanok.cellular;

import javax.swing.*;

public class CellularFrame extends JFrame {

    public CellularFrame() {
        CellularPanel cellularPanel = new CellularPanel();
        this.setTitle("Cellular GAME");
        this.setContentPane(cellularPanel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setLocation(10, 10);
        this.setVisible(true);
    }
}
