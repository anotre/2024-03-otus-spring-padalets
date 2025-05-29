package ru.otus.realEstateClassifieds.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyObjectDto {
    private long id;

    @Valid
    private AddressDto address;

    @PositiveOrZero(message = "")
    private float totalArea;

    @PositiveOrZero(message = "")
    private float kitchenArea;

    @PositiveOrZero(message = "")
    private float livingArea;

    @PositiveOrZero(message = "")
    private float residentialFloorArea;

    @Valid
    private ConstructionMethodDto constructionMethod;

    @PositiveOrZero(message = "")
    @Max(value = 255, message = "")
    private int roomNumber;

    @PositiveOrZero(message = "")
    @Max(value = 255, message = "")
    private int bathroomNumber;

    @PositiveOrZero(message = "")
    private float ceilingHeight;

    @Valid
    private BalconyTypeDto balconyType;

    @PositiveOrZero(message = "")
    @Max(value = 255, message = "")
    private int floorNumber;

    @Valid
    private FurnishedTypeDto furnishedType;
}
