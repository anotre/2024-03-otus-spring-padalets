package ru.otus.realEstateClassifieds.services;

import ru.otus.realEstateClassifieds.dto.BalconyTypeDto;

import java.util.List;

public interface BalconyTypeService {
    List<BalconyTypeDto> findAll();

    BalconyTypeDto create(BalconyTypeDto balconyType);

    BalconyTypeDto update(BalconyTypeDto balconyType);

    void deleteById(long id);
}