package ru.otus.realEstateClassifieds.services;

import ru.otus.realEstateClassifieds.dto.CountryDto;

import java.util.List;
import java.util.Optional;

public interface CountryService {
    Optional<CountryDto> findById(long id);

    List<CountryDto> findAll();
}

