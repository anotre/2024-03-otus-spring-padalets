package ru.otus.realEstateClassifieds.dto.mappers;

import org.mapstruct.Mapper;
import ru.otus.realEstateClassifieds.dto.BalconyTypeDto;
import ru.otus.realEstateClassifieds.models.BalconyType;

@Mapper(componentModel = "spring")
public interface BalconyTypeMapper {
    BalconyTypeDto entityToDto(BalconyType entity);

    BalconyType dtoToEntity(BalconyTypeDto dto);
}
