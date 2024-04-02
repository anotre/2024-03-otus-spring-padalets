package ru.otus.hw.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ResourceProviderImpl implements ResourceProvider {
    @Override
    public BufferedReader getBufferedReaderForResource(String filename) throws IOException {
        var classLoader = this.getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream(filename);

        if (inputStream == null) {
            throw new IOException("Resource could not be found.");
        }

        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }
}
