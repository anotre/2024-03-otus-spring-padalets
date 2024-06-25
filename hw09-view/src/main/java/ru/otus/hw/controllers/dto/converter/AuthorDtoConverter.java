package ru.otus.hw.controllers.dto.converter;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.converter.DtoConverter;
import ru.otus.hw.models.Author;
import ru.otus.hw.controllers.dto.AuthorDto;

@Component
public class AuthorDtoConverter implements DtoConverter<Author, AuthorDto> {

    @Override
    public AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }

    @Override
    public Author toDomain(AuthorDto authorDto) {
        return new Author(authorDto.getId(), authorDto.getFullName());
    }
}
