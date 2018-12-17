package com.company;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    private JTextArea outArea;
    private Timer timer;

    GUI() {
        super("Heldensagen vom Kosmosinsel");
        setFrameProperties();
        initComponents();
        fullscreen();
        this.pack();
    }

    private void setFrameProperties() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void fullscreen() {
        this.setAlwaysOnTop(rootPaneCheckingEnabled);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //this.setUndecorated(true);
    }

    private void initComponents() {
        outArea = new JTextArea(UnicodeFrame.WIDTH, UnicodeFrame.HEIGHT);
        outArea.setEditable(false);
        outArea.setBackground(Color.BLACK);
        outArea.setForeground(Color.WHITE);
        outArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        outArea.setOpaque(true);
        this.add(outArea);
    }

    public void setOutArea(String text) {
        outArea.setText(text);
    }

    public void mainLoop() {
        timer = new Timer(UnicodeFrame.TICK_DELAY, e -> {
            UnicodeFrame.nextFrame(this);
        });
        timer.start();
    }
}
