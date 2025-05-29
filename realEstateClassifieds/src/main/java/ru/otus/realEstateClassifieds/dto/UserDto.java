package ru.otus.realEstateClassifieds.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private long id;

    @NotBlank(message = "")
    private String login;

    @NotBlank(message = "")
    private String firstName;

    @NotBlank(message = "")
    private String lastName;

    @NotBlank(message = "")
    private String patronymic;

    @NotBlank(message = "")
    private String password;

    @Valid
    private EmailDto email;

    @Valid
    private PhoneNumberDto phoneNumber;
}
