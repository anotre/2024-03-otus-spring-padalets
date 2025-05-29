package ru.otus.realEstateClassifieds.dto.mappers;

import org.mapstruct.Mapper;
import ru.otus.realEstateClassifieds.dto.StreetDto;
import ru.otus.realEstateClassifieds.models.Street;

@Mapper(componentModel = "spring")
public interface StreetMapper {
    StreetDto entityToDto(Street entity);

    Street dtoToEntity(StreetDto dto);
}
