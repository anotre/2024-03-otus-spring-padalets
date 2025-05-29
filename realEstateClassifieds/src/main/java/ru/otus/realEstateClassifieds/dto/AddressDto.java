package ru.otus.realEstateClassifieds.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
    private long id;

    private String apartmentNumber;

    @NotBlank(message = "")
    private String buildingNumber;

    @Valid
    private StreetDto street;
}