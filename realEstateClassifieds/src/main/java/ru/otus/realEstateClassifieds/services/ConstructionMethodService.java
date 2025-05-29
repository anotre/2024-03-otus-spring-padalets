package ru.otus.realEstateClassifieds.services;

import ru.otus.realEstateClassifieds.dto.ConstructionMethodDto;

import java.util.List;

public interface ConstructionMethodService {
    List<ConstructionMethodDto> findAll();

    ConstructionMethodDto create(ConstructionMethodDto constructionMethod);

    ConstructionMethodDto update(ConstructionMethodDto constructionMethod);

    void deleteById(long id);
}