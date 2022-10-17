package com.pp.messages.file;

public interface FileAssistant {

    /**
     * function to write line at the end of the file
     * it's an interface because in future we might implement different type of file extension
     * for example we might change txt extension to csv
     * @param message
     */
    void writeToTheEndOfFile(String message);
}
