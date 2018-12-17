package com.company;

import java.io.IOException;

public class LOGH {

    private static GUI gui;

    public static void main(String[] args) {
        gui = new GUI();
        try {
            UnicodeFrame.displayTitle(gui);
        } catch(IOException e) {
            e.printStackTrace();
        }
        gui.mainLoop();
    }
}
