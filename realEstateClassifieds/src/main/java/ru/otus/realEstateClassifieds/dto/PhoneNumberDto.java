package ru.otus.realEstateClassifieds.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneNumberDto {
    private long id;

    @NotBlank(message = "")
    @Size(min = 4, max = 18, message = "")
    private String phoneNumber;
}
