package ru.otus.realEstateClassifieds.services;

import ru.otus.realEstateClassifieds.dto.SettlementDto;

import java.util.List;

public interface SettlementService {
    List<SettlementDto> findByRegionId(long id);
}