package ru.otus.hw.controllers.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {
    @Min(value = 1, message = "{genre-field-should-not-be-blank-validation-message}")
    private long id;

    private String name;
}
