package ru.otus.realEstateClassifieds.dto.mappers;

import org.mapstruct.Mapper;
import ru.otus.realEstateClassifieds.dto.ClassifiedDto;
import ru.otus.realEstateClassifieds.models.Classified;

@Mapper(componentModel = "spring")
public interface ClassifiedMapper {
    ClassifiedDto entityToDto(Classified entity);

    Classified dtoToEntity(ClassifiedDto dto);
}
