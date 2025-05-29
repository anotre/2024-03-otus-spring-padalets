package ru.otus.realEstateClassifieds.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.realEstateClassifieds.dto.FurnishedTypeDto;
import ru.otus.realEstateClassifieds.dto.mappers.FurnishedTypeMapper;
import ru.otus.realEstateClassifieds.exceptions.EntityAlreadyExistsException;
import ru.otus.realEstateClassifieds.exceptions.EntityNonExistsException;
import ru.otus.realEstateClassifieds.repositories.FurnishedTypeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FurnishedTypeServiceImpl implements FurnishedTypeService {
    private final FurnishedTypeRepository repository;

    private final FurnishedTypeMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<FurnishedTypeDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    public FurnishedTypeDto create(FurnishedTypeDto furnishedType) {
        if (furnishedType.getId() != 0) {
            throw new EntityAlreadyExistsException("The entity to be created must not exist");
        }

        return this.save(furnishedType);
    }

    @Override
    @Transactional
    public FurnishedTypeDto update(FurnishedTypeDto furnishedType) {
        if (furnishedType.getId() == 0) {
            throw new EntityNonExistsException("The entity to be created must exist");
        }

        return this.save(furnishedType);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    private FurnishedTypeDto save(FurnishedTypeDto furnishedType) {
        var savedEntity = this.repository.save(this.mapper.dtoToEntity(furnishedType));
        return this.mapper.entityToDto(savedEntity);
    }
}