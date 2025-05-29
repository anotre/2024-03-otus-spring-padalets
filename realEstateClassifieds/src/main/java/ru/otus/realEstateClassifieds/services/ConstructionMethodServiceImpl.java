package ru.otus.realEstateClassifieds.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.realEstateClassifieds.dto.ConstructionMethodDto;
import ru.otus.realEstateClassifieds.dto.mappers.ConstructionMethodMapper;
import ru.otus.realEstateClassifieds.exceptions.EntityAlreadyExistsException;
import ru.otus.realEstateClassifieds.exceptions.EntityNonExistsException;
import ru.otus.realEstateClassifieds.repositories.ConstructionMethodRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConstructionMethodServiceImpl implements ConstructionMethodService {
    private final ConstructionMethodRepository repository;

    private final ConstructionMethodMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ConstructionMethodDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    public ConstructionMethodDto create(ConstructionMethodDto constructionMethod) {
        if (constructionMethod.getId() != 0) {
            throw new EntityAlreadyExistsException("The entity to be created must not exist");
        }

        return this.save(constructionMethod);
    }

    @Override
    @Transactional
    public ConstructionMethodDto update(ConstructionMethodDto constructionMethod) {
        if (constructionMethod.getId() == 0) {
            throw new EntityNonExistsException("The entity to be updated must  exist");
        }

        return this.save(constructionMethod);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        this.repository.deleteById(id);
    }

    private ConstructionMethodDto save(ConstructionMethodDto constructionMethod) {
        var entity = repository.save(mapper.dtoToEntity(constructionMethod));
        return mapper.entityToDto(entity);
    }
}