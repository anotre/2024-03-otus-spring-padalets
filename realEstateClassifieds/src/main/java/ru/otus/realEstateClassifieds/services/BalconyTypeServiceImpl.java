package ru.otus.realEstateClassifieds.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.realEstateClassifieds.dto.BalconyTypeDto;
import ru.otus.realEstateClassifieds.dto.mappers.BalconyTypeMapper;
import ru.otus.realEstateClassifieds.exceptions.EntityAlreadyExistsException;
import ru.otus.realEstateClassifieds.exceptions.EntityNonExistsException;
import ru.otus.realEstateClassifieds.repositories.BalconyTypeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BalconyTypeServiceImpl implements BalconyTypeService {
    private final BalconyTypeRepository repository;

    private final BalconyTypeMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<BalconyTypeDto> findAll() {
        return repository.findAll().stream().map(mapper::entityToDto).toList();
    }

    @Override
    @Transactional
    public BalconyTypeDto create(BalconyTypeDto balconyType) {
        if (balconyType.getId() != 0) {
            throw new EntityAlreadyExistsException("The entity to be created must not exist");
        }

        return this.save(balconyType);
    }

    @Override
    @Transactional
    public BalconyTypeDto update(BalconyTypeDto balconyType) {
        if (balconyType.getId() == 0) {
            throw new EntityNonExistsException("The entity to be updated must  exist");
        }

        return this.save(balconyType);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    private BalconyTypeDto save(BalconyTypeDto balconyType) {
        var savedEntity = repository.save(mapper.dtoToEntity(balconyType));
        return mapper.entityToDto(savedEntity);
    }
}