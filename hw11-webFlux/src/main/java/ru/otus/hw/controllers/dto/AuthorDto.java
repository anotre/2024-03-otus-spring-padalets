package ru.otus.hw.controllers.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {
    @NotEmpty(message = "{author-field-should-not-be-blank-validation-message}")
    private String id;

    private String fullName;
}
