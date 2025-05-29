package ru.otus.realEstateClassifieds.dto.mappers;

import org.mapstruct.Mapper;
import ru.otus.realEstateClassifieds.dto.SettlementDto;
import ru.otus.realEstateClassifieds.models.Settlement;

@Mapper(componentModel = "spring")
public interface SettlementMapper {
    SettlementDto entityToDto(Settlement entity);

    Settlement dtoToEntity(SettlementDto dto);
}
