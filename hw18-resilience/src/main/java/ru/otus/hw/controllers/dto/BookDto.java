package ru.otus.hw.controllers.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private long id;

    @NotBlank(message = "{title-field-should-not-be-blank-validation-message}")
    @Size(min = 2, max = 255, message = "{title-field-should-be-expected-size-validation-message}")
    private String title;

    @Valid
    @NotNull(message = "{author-field-should-not-be-blank-validation-message}")
    private AuthorDto author;

    @Valid
    @NotNull(message = "{genre-field-should-not-be-blank-validation-message}")
    private GenreDto genre;
}
