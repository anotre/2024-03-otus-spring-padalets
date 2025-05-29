package ru.otus.realEstateClassifieds.controllers.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.realEstateClassifieds.dto.SettlementDto;
import ru.otus.realEstateClassifieds.services.SettlementService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер взаимодействия с сущностями городов")
public class SettlementController {
    private final SettlementService settlementService;

    @GetMapping("/settlements/{id}")
    @Operation(summary = "Поиск всех населенных пунктов")
    public List<SettlementDto> findSettlementsByRegionId(@PathVariable long id) {
        return settlementService.findByRegionId(id);
    }
}
