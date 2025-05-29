package ru.otus.realEstateClassifieds.dto.mappers;

import org.mapstruct.Mapper;
import ru.otus.realEstateClassifieds.dto.PropertyObjectDto;
import ru.otus.realEstateClassifieds.models.PropertyObject;

@Mapper(componentModel = "spring")
public interface PropertyObjectMapper {
    PropertyObjectDto entityToDto(PropertyObject entity);

    PropertyObject dtoToEntity(PropertyObjectDto dto);
}
