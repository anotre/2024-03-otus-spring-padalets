package ru.otus.hw.dao;

import java.util.List;

public interface Dao<T> {
    int size();

    T get(Long id);

    List<T> getAll();
}
