package ru.otus.realEstateClassifieds.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.realEstateClassifieds.dto.CountryDto;
import ru.otus.realEstateClassifieds.dto.mappers.CountryMapper;
import ru.otus.realEstateClassifieds.repositories.CountryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository repository;

    private final CountryMapper mapper;

    @Override
    public Optional<CountryDto> findById(long id) {
        return repository.findById(id).map(this.mapper::entityToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CountryDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::entityToDto)
                .toList();
    }
}