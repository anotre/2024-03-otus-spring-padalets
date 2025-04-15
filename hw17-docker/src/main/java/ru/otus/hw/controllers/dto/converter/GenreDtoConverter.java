package ru.otus.hw.controllers.dto.converter;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.converter.DtoConverter;
import ru.otus.hw.models.Genre;
import ru.otus.hw.controllers.dto.GenreDto;

@Component
public class GenreDtoConverter implements DtoConverter<Genre, GenreDto> {
    @Override
    public GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

    @Override
    public Genre toDomain(GenreDto genreDto) {
        return new Genre(genreDto.getId(), genreDto.getName());
    }
}
