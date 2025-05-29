package ru.otus.realEstateClassifieds.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.realEstateClassifieds.dto.PropertyObjectDto;
import ru.otus.realEstateClassifieds.dto.mappers.PropertyObjectMapper;
import ru.otus.realEstateClassifieds.exceptions.EntityAlreadyExistsException;
import ru.otus.realEstateClassifieds.exceptions.EntityNonExistsException;
import ru.otus.realEstateClassifieds.repositories.PropertyObjectRepository;

@Service
@RequiredArgsConstructor
public class PropertyObjectServiceImpl implements PropertyObjectService {
    private final PropertyObjectRepository repository;

    private final PropertyObjectMapper mapper;

    @Override
    @Transactional
    public PropertyObjectDto create(PropertyObjectDto propertyObject) {
        if (propertyObject.getId() != 0) {
            throw new EntityAlreadyExistsException("The entity to be created must not exist");
        }

        return this.save(propertyObject);
    }

    @Override
    @Transactional
    public PropertyObjectDto update(PropertyObjectDto propertyObject) {
        if (propertyObject.getId() == 0) {
            throw new EntityNonExistsException("The entity to be created must exist");
        }

        return this.save(propertyObject);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    private PropertyObjectDto save(PropertyObjectDto propertyObject) {
        var savedEntity = this.repository.save(this.mapper.dtoToEntity(propertyObject));
        return this.mapper.entityToDto(savedEntity);
    }
}