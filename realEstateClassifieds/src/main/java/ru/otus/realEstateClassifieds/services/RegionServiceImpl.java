package ru.otus.realEstateClassifieds.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.realEstateClassifieds.dto.RegionDto;
import ru.otus.realEstateClassifieds.dto.mappers.RegionMapper;
import ru.otus.realEstateClassifieds.exceptions.EntityNotFoundException;
import ru.otus.realEstateClassifieds.repositories.RegionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {
    private final RegionRepository regionRepository;

    private final CountryService countryService;

    private final RegionMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<RegionDto> findById(long id) {
        return regionRepository.findById(id).map(this.mapper::entityToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegionDto> findByCountryId(long id) {
        countryService.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Country with id %d not found".formatted(id))
        );

        return this.regionRepository.findByCountryId(id).stream()
                .map(this.mapper::entityToDto)
                .toList();
    }
}