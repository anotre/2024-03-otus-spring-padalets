package ru.otus.realEstateClassifieds.dto.mappers;

import org.mapstruct.Mapper;
import ru.otus.realEstateClassifieds.dto.ConstructionMethodDto;
import ru.otus.realEstateClassifieds.models.ConstructionMethod;

@Mapper(componentModel = "spring")
public interface ConstructionMethodMapper {
    ConstructionMethodDto entityToDto(ConstructionMethod entity);

    ConstructionMethod dtoToEntity(ConstructionMethodDto dto);
}
