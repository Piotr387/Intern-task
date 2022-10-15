package com.pp.common;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
class FileUtilities {

    private static final int BUFFER_SIZE = 1024;

    String readFile(String location) {

        var classPathResource = new ClassPathResource(location);

        try (InputStream inputStream = classPathResource.getInputStream()) {
            return toString(inputStream);
        } catch (IOException ioe) {
            throw new ResourceAccessException(ioe.getMessage(), ioe);
        }
    }

    private static String toString(InputStream inputStream) throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int length;

        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        return result.toString(StandardCharsets.UTF_8);
    }
}
