package ru.otus.hw.controllers.dto.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.converter.DtoConverter;
import ru.otus.hw.models.Book;
import ru.otus.hw.controllers.dto.BookDto;

@Component
@RequiredArgsConstructor
public class BookDtoConverter implements DtoConverter<Book, BookDto> {
    private final AuthorDtoConverter authorDtoConverter;

    private final GenreDtoConverter genreDtoConverter;

    @Override
    public BookDto toDto(Book book) {
        var authorDto = authorDtoConverter.toDto(book.getAuthor());
        var genreDto = genreDtoConverter.toDto(book.getGenre());

        return new BookDto(
                book.getId(),
                book.getTitle(),
                authorDto,
                genreDto);
    }

    @Override
    public Book toDomain(BookDto bookDto) {
        var author = authorDtoConverter.toDomain(bookDto.getAuthor());
        var genre = genreDtoConverter.toDomain(bookDto.getGenre());

        return new Book(
                bookDto.getId(),
                bookDto.getTitle(),
                author,
                genre
        );
    }
}
