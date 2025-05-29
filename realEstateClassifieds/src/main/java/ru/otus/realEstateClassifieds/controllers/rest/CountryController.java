package ru.otus.realEstateClassifieds.controllers.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.realEstateClassifieds.dto.CountryDto;
import ru.otus.realEstateClassifieds.services.CountryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер взаимодействия с сущностями стран")
public class CountryController {
    private final CountryService countryService;

    @GetMapping("/countries")
    @Operation(summary = "Поиск всех стран")
    public List<CountryDto> findAll() {
        return countryService.findAll();
    }
}
