package ru.otus.realEstateClassifieds.dto.mappers;

import org.mapstruct.Mapper;
import ru.otus.realEstateClassifieds.dto.AddressDto;
import ru.otus.realEstateClassifieds.models.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDto entityToDto(Address entity);

    Address dtoToEntity(AddressDto dto);
}
