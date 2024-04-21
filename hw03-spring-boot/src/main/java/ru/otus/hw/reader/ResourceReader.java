package ru.otus.hw.reader;

import java.io.IOException;
import java.io.InputStream;

public interface ResourceReader {
    InputStream getResourceAsStream(String filename) throws IOException;
}
