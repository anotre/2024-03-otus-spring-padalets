package ru.otus.realEstateClassifieds.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClassifiedSearchDto {
    private long settlementId;

    private long regionId;

    private long countryId;

    private long priceFrom;

    private long priceTo;

    private float totalAreaFrom;

    private float totalAreaTo;

    private float kitchenAreaFrom;

    private float kitchenAreaTo;

    private float livingAreaFrom;

    private float livingAreaTo;

    private float residentialFloorAreaFrom;

    private float residentialFloorAreaTo;

    private float ceilingHeightFrom;

    private float ceilingHeightTo;

    private List<Long> constructionMethods;

    private List<Long> balconyTypes;

    private List<Integer> roomNumbers;

    private int floorNumberFrom;

    private int floorNumberTo;
}