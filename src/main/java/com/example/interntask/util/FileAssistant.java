package com.example.interntask.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileAssistant {
    private static final String FILENAME = "powiadomienia.txt";

    public static void writeToTheEndOfFile(String message) {
        try (FileWriter fileWriter = new FileWriter(FILENAME, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){
            bufferedWriter.append(message);
        } catch (IOException e) {
            throw new RuntimeException("There was an error wrting to file");
        }
    }
}
