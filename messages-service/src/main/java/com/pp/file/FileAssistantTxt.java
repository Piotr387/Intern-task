package com.pp.file;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class FileAssistantTxt implements FileAssistant {

    private static final String FILENAME = "powiadomienia.txt";

    public void writeToTheEndOfFile(String message) {
        try (FileWriter fileWriter = new FileWriter(FILENAME, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.append(message);
        } catch (IOException e) {
            throw new RuntimeException("There was an error writing to file");
        }
    }
}
