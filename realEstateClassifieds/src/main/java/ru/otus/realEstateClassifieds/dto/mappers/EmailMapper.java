package ru.otus.realEstateClassifieds.dto.mappers;

import org.mapstruct.Mapper;
import ru.otus.realEstateClassifieds.dto.EmailDto;
import ru.otus.realEstateClassifieds.models.Email;

@Mapper(componentModel = "spring")
public interface EmailMapper {
    EmailDto entityToDto(Email entity);

    Email dtoToEntity(EmailDto dto);
}
