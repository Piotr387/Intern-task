package com.example.interntask.file;

import com.example.interntask.responde.ErrorMessages;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileAssistantTxt implements FileAssistant {
    private static final String FILENAME = "powiadomienia.txt";

    public void writeToTheEndOfFile(String message) {
        // Try catch block with resources
        try (FileWriter fileWriter = new FileWriter(FILENAME, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){
            bufferedWriter.append(message);
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessages.ERROR_WRITING_FILE.getErrorMessage());
        }
    }
}
