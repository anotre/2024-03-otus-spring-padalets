package ru.otus.realEstateClassifieds.services;

import ru.otus.realEstateClassifieds.dto.StreetDto;

import java.util.List;

public interface StreetService {
    List<StreetDto> findSettlementId(long id);
}