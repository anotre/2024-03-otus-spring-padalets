package ru.otus.hw.reader;

import java.io.IOException;
import java.io.InputStream;

public class ResourceReaderImpl implements ResourceReader {
    @Override
    public InputStream getResourceAsStream(String filename) throws IOException {
        var classLoader = this.getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream(filename);

        if (inputStream == null) {
            throw new IOException("Resource could not be found.");
        }

        return inputStream;
    }
}
