package ru.otus.realEstateClassifieds.dto.mappers;

import org.mapstruct.Mapper;
import ru.otus.realEstateClassifieds.dto.UserDto;
import ru.otus.realEstateClassifieds.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto entityToDto(User address);

    User dtoToEntity(UserDto addressDto);
}
