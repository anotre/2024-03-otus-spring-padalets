package ru.otus.hw.dao;

import ru.otus.hw.domain.Addressee;

import java.util.List;

public interface AddresseeDao {
    int size();

    Addressee get(Long id);

    List<Addressee> getAll();
}
