package ru.otus.realEstateClassifieds.dto.mappers;

import org.mapstruct.Mapper;
import ru.otus.realEstateClassifieds.dto.CountryDto;
import ru.otus.realEstateClassifieds.models.Country;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    CountryDto entityToDto(Country entity);

    Country dtoToEntity(CountryDto dto);
}
