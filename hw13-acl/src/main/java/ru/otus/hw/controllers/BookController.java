package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.otus.hw.controllers.dto.BookDto;
import ru.otus.hw.controllers.exceptions.NotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    private final CommentService commentService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/books")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public String booksList(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);

        return "books-list";
    }

    @GetMapping("/books/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public String book(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id).orElseThrow(NotFoundException::new);
        var comments = commentService.findByBookId(id);
        model.addAttribute("bookDto", book);
        model.addAttribute("comments", comments);

        return "book";
    }

    @GetMapping("/books/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public String create(Model model) {
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("bookDto", new BookDto());
        return "create-book";
    }

    @GetMapping(value = "/books/edit/{id}")
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Book', 'WRITE') || hasAuthority('ROLE_ADMIN')")
    public String edit(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("bookDto", book);

        return "edit-book";
    }

    @PostMapping("/books/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public String create(@Valid BookDto bookDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "create-book";
        }

        bookService.insert(
                bookDto.getTitle(),
                bookDto.getAuthor().getId(),
                bookDto.getGenre().getId());

        return "redirect:/books";
    }

    @PostMapping("/books/edit")
    @PreAuthorize("hasPermission(#bookDto.id, 'ru.otus.hw.models.Book', 'WRITE') || hasAuthority('ROLE_ADMIN')")
    public String edit(@Valid BookDto bookDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "edit-book";
        }

        bookService.update(
                bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getAuthor().getId(),
                bookDto.getGenre().getId());

        return "redirect:/books";
    }

    @PostMapping("/books/delete/{id}")
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Book', 'DELETE') || hasAuthority('ROLE_ADMIN')")
    public String deleteById(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException exception) {
        return "not-found";
    }
}
