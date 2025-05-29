package ru.otus.realEstateClassifieds.services;

import ru.otus.realEstateClassifieds.dto.FurnishedTypeDto;

import java.util.List;

public interface FurnishedTypeService {
    List<FurnishedTypeDto> findAll();

    FurnishedTypeDto create(FurnishedTypeDto furnishedType);

    FurnishedTypeDto update(FurnishedTypeDto furnishedType);

    void deleteById(long id);
}
