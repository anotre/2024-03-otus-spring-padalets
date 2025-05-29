package ru.otus.realEstateClassifieds.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegionDto {
    private long id;

    @Size(min = 2, max = 255, message = "")
    @NotBlank(message = "")
    private String name;

    @Valid
    private CountryDto country;
}
