package ru.otus.hw.controllers.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {
    @Min(value = 1, message = "{author-field-should-not-be-blank-validation-message}")
    private long id;

    private String fullName;
}
