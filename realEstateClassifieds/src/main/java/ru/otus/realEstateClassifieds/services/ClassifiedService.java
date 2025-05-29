package ru.otus.realEstateClassifieds.services;

import ru.otus.realEstateClassifieds.dto.ClassifiedDto;
import ru.otus.realEstateClassifieds.dto.ClassifiedSearchDto;

import java.util.List;
import java.util.Optional;

public interface ClassifiedService {
    Optional<ClassifiedDto> findById(long id);

    List<ClassifiedDto> findByParams(ClassifiedSearchDto searchRequest);

    ClassifiedDto create(ClassifiedDto classified);

    ClassifiedDto update(ClassifiedDto classified);

    void deleteById(long id);
}