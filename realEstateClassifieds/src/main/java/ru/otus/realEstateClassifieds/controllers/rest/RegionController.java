package ru.otus.realEstateClassifieds.controllers.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.realEstateClassifieds.dto.RegionDto;
import ru.otus.realEstateClassifieds.services.RegionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер взаимодействия с сущностями регионов")
public class RegionController {
    private final RegionService regionService;

    @GetMapping("/regions")
    @Operation(summary = "Поиск всех регионов")
    public List<RegionDto> findAll() {
        return regionService.findByCountryId(0);
    }
}
