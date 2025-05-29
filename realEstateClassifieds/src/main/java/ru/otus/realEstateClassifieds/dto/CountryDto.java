package ru.otus.realEstateClassifieds.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryDto {
    private long id;

    @NotBlank(message = "")
    @Size(min = 2, max = 127, message = "")
    private String name;
}
