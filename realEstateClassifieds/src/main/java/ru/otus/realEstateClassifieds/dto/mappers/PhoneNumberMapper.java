package ru.otus.realEstateClassifieds.dto.mappers;

import org.mapstruct.Mapper;
import ru.otus.realEstateClassifieds.dto.PhoneNumberDto;
import ru.otus.realEstateClassifieds.models.PhoneNumber;

@Mapper(componentModel = "spring")
public interface PhoneNumberMapper {
    PhoneNumberDto entityToDto(PhoneNumber entity);

    PhoneNumber dtoToEntity(PhoneNumberDto dto);
}
