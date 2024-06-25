package ru.otus.hw.controllers.dto.converter;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.converter.DtoConverter;
import ru.otus.hw.models.Book;
import ru.otus.hw.controllers.dto.BookDto;

@Component
public class BookDtoConverter implements DtoConverter<Book, BookDto> {

    @Override
    public BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getId(),
                book.getGenre().getId());
    }

    @Override
    public Book toDomain(BookDto bookDto) {
        return new Book();
    }
}
