package ru.otus.realEstateClassifieds.dto.mappers;

import org.mapstruct.Mapper;
import ru.otus.realEstateClassifieds.dto.RegionDto;
import ru.otus.realEstateClassifieds.models.Region;

@Mapper(componentModel = "spring")
public interface RegionMapper {
    RegionDto entityToDto(Region entity);

    Region dtoToEntity(RegionDto dto);
}
