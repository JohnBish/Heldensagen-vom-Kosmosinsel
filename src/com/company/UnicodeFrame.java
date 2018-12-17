package com.company;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UnicodeFrame {

    private static final String ASSET_PATH = "./assets/";

    public static final int WIDTH = 200;
    public static final int HEIGHT = 72;
    public static final int TICK_RATE = 30;
    public static final int TICK_DELAY = 1000 / TICK_RATE;

    private static List<String> frameBuffer = new ArrayList<>();

    public static Timer titleTimer;

    public enum Direction {
        CENTERED, LEFT, RIGHT, TOP, BOTTOM
    }

    public static void pushFrame(char[][] frame) {
        frameBuffer.add(charArrayToString(frame));
    }

    public static void nextFrame(GUI gui) {
        if(frameBuffer.size() > 0) gui.setOutArea(frameBuffer.remove(0));
    }

    public static void displayTitle(GUI gui) throws IOException {
        final char[][] titleCharArray1;
        final char[][] titleCharArray2;
        final char[][] titleCharArray3;
        final char[][] titleCharArray4;
        final char[][] galaxyCharArray1;
        final char[][] galaxyCharArray2;
        final char[][] galaxyCharArray3;
        final char[][] galaxyCharArray4;
        final char[][] galaxyCharArray5;
        titleCharArray1 = centerHorizontal(fileToCharArray("Heldensagen vom Kosmosinsel1.txt"), WIDTH);
        titleCharArray2 = centerHorizontal(fileToCharArray("Heldensagen vom Kosmosinsel2.txt"), WIDTH);
        titleCharArray3 = centerHorizontal(fileToCharArray("Heldensagen vom Kosmosinsel3.txt"), WIDTH);
        titleCharArray4 = centerHorizontal(fileToCharArray("Heldensagen vom Kosmosinsel4.txt"), WIDTH);
        galaxyCharArray1 = fileToCharArray("Galaxy1.txt");
        galaxyCharArray2 = fileToCharArray("Galaxy2.txt");
        galaxyCharArray3 = fileToCharArray("Galaxy3.txt");
        galaxyCharArray4 = fileToCharArray("Galaxy4.txt");
        galaxyCharArray5 = fileToCharArray("Galaxy5.txt");

        //Animation class would be better but this'll work for now;
        AtomicInteger galaxyState = new AtomicInteger(0);
        AtomicInteger titleOpacity = new AtomicInteger(0);
        titleTimer = new Timer(300, e -> {
            char[][] galaxy;
            char[][] title;
            switch(galaxyState.get()) {
                case 0:
                    galaxy = galaxyCharArray1;
                    break;
                case 1:
                    galaxy = galaxyCharArray2;
                    break;
                case 2:
                    galaxy = galaxyCharArray3;
                    break;
                case 3:
                    galaxy = galaxyCharArray4;
                    break;
                case 4:
                    galaxy = galaxyCharArray5;
                    break;
                case 5:
                    galaxy = galaxyCharArray4;
                    break;
                case 6:
                    galaxy = galaxyCharArray3;
                    break;
                case 7:
                    galaxy = galaxyCharArray2;
                    break;
                default:
                    galaxy = galaxyCharArray1;
            }

            int o = titleOpacity.get();
            if(o >= 27) {
                title = new char[0][0];
            } else if(o >= 26) {
                title = titleCharArray1;
            } else if(o >= 25) {
                title = titleCharArray2;
            } else if(o >= 24) {
                title = titleCharArray3;
            } else if(o >= 12) {
                title = titleCharArray4;
            } else if(o >= 11) {
                title = titleCharArray3;
            } else if(o >= 10) {
                title = titleCharArray2;
            } else if(o >= 9) {
                title = titleCharArray1;
            } else {
                title = new char[0][0];
            }

            pushFrame(overlayCharArray(title, galaxy));
            titleOpacity.incrementAndGet();
            galaxyState.set((galaxyState.get() + 1) % 7);
        });
        titleTimer.start();
    }

    private static String charArrayToString(char[][] frame) {
        StringBuilder sb = new StringBuilder();

        String spacer = "";
        for(char[] l: frame) {
            sb.append(spacer);
            StringBuilder line = new StringBuilder();
            for(char c: l) {
                String nextChar = c == '\u0000' ? " ": Character.toString(c);
                line.append(nextChar);
            }
            sb.append(line);
            spacer = "\n";
        }

        return sb.toString();
    }

    public static char[][] fileToCharArray(String fName) throws IOException {
        InputStream inputStream = new FileInputStream(ASSET_PATH + fName);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-16"));
        List<char[]> charBuffer = new ArrayList<>();
        String line;
        int wMax = 0;

        line = in.readLine();
        while(line != null) {
            char[] chars = line.toCharArray();
            int length = chars.length;
            if(length > wMax) wMax = length;
            charBuffer.add(chars);
            line = in.readLine();
        }

        char[][] charArray = new char[charBuffer.size()][wMax];
        for(int i=0; i<charBuffer.size(); i++) {
            char[] row = charBuffer.get(i);
            System.arraycopy(row, 0, charArray[i], 0, row.length);
        }

        return charArray;
    }

    public static String fileToFrameString(String fName) throws IOException {
        InputStream inputStream = new FileInputStream(ASSET_PATH + fName);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-16"));
        StringBuilder sb = new StringBuilder();
        String line;

        line = in.readLine();
        String spacer = "";
        while(line != null) {
            line = in.readLine();
            sb.append(spacer);
            sb.append(line);
            spacer = "\n";
        }

        return sb.toString();
    }

    private static char[][] overlayCharArray(char[][] arr1, char[][] arr2) {
        int width = arr2[0].length;
        int height = arr2.length;
        char[][] result = new char[height][width];

        for(int i=0; i<height; i++) {
            for(int j=0; j<width; j++) {
                char c = i < arr1.length && j < arr1[0].length ? arr1[i][j]: ' ';
                result[i][j] = c != ' ' && c != '\u0000' ? c: arr2[i][j];
            }
        }

        return result;
    }

    private static char[][] centerHorizontal(char[][] frame, int width) {
        int height = frame.length;
        int frameW = frame[0].length;
        char[][] centeredFrame = new char[height][width];
        int offset = (width - frameW) / 2;
        for(int i=0; i<height; i++) {
            System.arraycopy(frame[i], 0, centeredFrame[i], offset, frameW);
        }
        return centeredFrame;
    }
}
