package ru.otus.hw.controllers.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {
    @NotEmpty(message = "{genre-field-should-not-be-blank-validation-message}")
    private String id;

    private String name;
}
