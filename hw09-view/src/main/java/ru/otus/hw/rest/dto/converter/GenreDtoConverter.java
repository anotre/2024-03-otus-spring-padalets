package ru.otus.hw.rest.dto.converter;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Genre;
import ru.otus.hw.rest.dto.GenreDto;

@Component
public class GenreDtoConverter {
    public GenreDto convert(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
