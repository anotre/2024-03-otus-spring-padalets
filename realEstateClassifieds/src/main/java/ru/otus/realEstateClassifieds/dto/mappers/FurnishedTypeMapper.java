package ru.otus.realEstateClassifieds.dto.mappers;

import org.mapstruct.Mapper;
import ru.otus.realEstateClassifieds.dto.FurnishedTypeDto;
import ru.otus.realEstateClassifieds.models.FurnishedType;

@Mapper(componentModel = "spring")
public interface FurnishedTypeMapper {
    FurnishedTypeDto entityToDto(FurnishedType entity);

    FurnishedType dtoToEntity(FurnishedTypeDto dto);
}
