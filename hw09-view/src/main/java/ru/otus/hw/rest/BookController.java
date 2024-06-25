package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.dto.converter.BookDtoConverter;
import ru.otus.hw.rest.exceptions.NotFoundException;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookDtoConverter bookDtoConverter;

    @GetMapping("/books")
    public String listPage(Model model) {
        var books = bookService.findAll().stream()
                .map(bookDtoConverter::toDto)
                .collect(Collectors.toList());
        model.addAttribute("books", books);
        return "booksList";
    }

    @GetMapping("/books/{id}")
    public String getById(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book", bookDtoConverter.toDto(book));
        return "book";
    }

    @PostMapping("/books")
    public String save(BookDto bookDto) {
        bookService.insert(bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId());
        return "redirect:/";
    }

    @PatchMapping("/books/{id}") // загвоздка с @PatchMapping
    public String updateById(BookDto bookDto, Model model) {
        bookService.update(
                bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getAuthorId(),
                bookDto.getGenreId());
        model.addAttribute(model);
        return "bookPage"; // после обновления следует возврат на страницу со списком
    }

    @DeleteMapping("/books/{id}")
    public String deleteById(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }
}
