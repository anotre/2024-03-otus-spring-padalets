package ru.otus.realEstateClassifieds.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {
    private long id;

    @Email(message = "")
    @NotBlank(message = "")
    private String email;
}
