package ru.otus.realEstateClassifieds.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.realEstateClassifieds.dto.SettlementDto;
import ru.otus.realEstateClassifieds.dto.mappers.SettlementMapper;
import ru.otus.realEstateClassifieds.repositories.SettlementRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SettlementServiceImpl implements SettlementService {

    private final SettlementRepository settlementRepository;

    private final RegionService regionService;

    private final SettlementMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<SettlementDto> findByRegionId(long id) {
        regionService.findById(id);

        return this.settlementRepository.findByRegionId(id).stream()
                .map(this.mapper::entityToDto)
                .toList();
    }
}