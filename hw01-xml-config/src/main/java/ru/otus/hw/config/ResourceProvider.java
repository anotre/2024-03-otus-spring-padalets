package ru.otus.hw.config;

import java.io.BufferedReader;
import java.io.IOException;

public interface ResourceProvider {
    BufferedReader getBufferedReaderForResource(String filename) throws IOException;
}