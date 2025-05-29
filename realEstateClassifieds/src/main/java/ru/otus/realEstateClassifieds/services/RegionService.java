package ru.otus.realEstateClassifieds.services;

import ru.otus.realEstateClassifieds.dto.RegionDto;

import java.util.List;
import java.util.Optional;

public interface RegionService {
    Optional<RegionDto> findById(long id);

    List<RegionDto> findByCountryId(long id);
}