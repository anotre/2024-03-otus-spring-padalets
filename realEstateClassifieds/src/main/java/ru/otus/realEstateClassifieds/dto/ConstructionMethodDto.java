package ru.otus.realEstateClassifieds.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConstructionMethodDto {
    private long id;

    @NotBlank(message = "")
    @Size(min = 3, max = 127, message = "")
    private String name;
}
