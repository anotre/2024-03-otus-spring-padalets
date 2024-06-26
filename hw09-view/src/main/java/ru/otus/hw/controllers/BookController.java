package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.controllers.dto.BookDto;
import ru.otus.hw.controllers.dto.converter.BookDtoConverter;
import ru.otus.hw.controllers.exceptions.NotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;
    @GetMapping("/books")
    public String booksList(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);

        return "books-list";
    }

    @GetMapping("/books/{id}")
    public String book(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);

        return "book";
    }

    @PostMapping("/books")
    public String create(BookDto bookDto) {
        bookService.insert(
                bookDto.getTitle(),
                bookDto.getAuthor().getId(),
                bookDto.getGenre().getId());

        return "redirect:/";
    }

    @GetMapping("/books/create")
    public String create(Model model) {
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "edit-book";
    }

    @GetMapping(value = "/books/edit", params = "id")
    // параметр запроса потому, что параметр пути должен быть точно, а параметр запроса опционален
    public String edit(@RequestParam("id") long id, Model model) { // проблема с id, я думал что в случае чего там будет 0, но, видимо, исключение
        var book = bookService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("book", book);
        return "edit-book";
    }

    @PatchMapping("/books")
    // в шаблоне только один метод отправки формы, так что либо добавить один шаблон, либо убирать этот метод
    public String updateById(BookDto bookDto) {
        bookService.update(
                bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getAuthor().getId(),
                bookDto.getGenre().getId());

        return "redirect:/books-list";
    }

    @DeleteMapping("/books/{id}")
    public String deleteById(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException ex) {
        ResponseEntity.badRequest();
        return "redirect:/not-found";
    }
}

/**
 * GET /books           // booksList
 * GET /books/{id}      // book
 * POST /books          // edit -> book
 * PATCH /books/{id}    // edit -> list
 * DELETE /books/{id}   // list -> list
 */
