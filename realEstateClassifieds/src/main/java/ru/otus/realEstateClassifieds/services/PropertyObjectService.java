package ru.otus.realEstateClassifieds.services;

import ru.otus.realEstateClassifieds.dto.PropertyObjectDto;

public interface PropertyObjectService {
    PropertyObjectDto create(PropertyObjectDto propertyObject);

    PropertyObjectDto update(PropertyObjectDto propertyObject);

    void deleteById(long id);
}