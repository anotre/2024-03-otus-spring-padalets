package ru.otus.realEstateClassifieds.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.realEstateClassifieds.dto.StreetDto;
import ru.otus.realEstateClassifieds.dto.mappers.StreetMapper;
import ru.otus.realEstateClassifieds.repositories.StreetRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StreetServiceImpl implements StreetService {
    private final StreetRepository repository;

    private final StreetMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<StreetDto> findSettlementId(long id) {
        return this.repository.findBySettlementId(id).stream()
                .map(this.mapper::entityToDto)
                .toList();
    }
}