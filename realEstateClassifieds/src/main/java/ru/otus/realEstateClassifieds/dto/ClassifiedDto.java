package ru.otus.realEstateClassifieds.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ClassifiedDto {
    private long id;

    @NotBlank(message = "")
    private String title;

    @Valid
    @NotNull(message = "")
    private UserDto seller;

    @Valid
    private PropertyObjectDto object;

    @Size(max = 2047, message = "")
    private String description;

    @PositiveOrZero(message = "")
    private long price;

    @NotNull(message = "")
    @PastOrPresent(message = "")
    private LocalDateTime createdAt;

    @PastOrPresent(message = "")
    private LocalDateTime updatedAt;
}
