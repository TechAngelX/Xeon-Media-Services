package com.xeon2035.audio.utils;

import java.io.IOException;

public class ClearTerminal {
    public static void clearTerminal() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder processBuilder;

            if (os.contains("win")) {
                processBuilder = new ProcessBuilder("cmd", "/c", "cls"); // Windows
            } else {
                processBuilder = new ProcessBuilder("clear"); // Linux/macOS
            }

            processBuilder.inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println("Could not clear terminal.");
        }
    }
}
