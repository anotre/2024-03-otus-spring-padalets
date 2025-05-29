package ru.otus.realEstateClassifieds.controllers.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.realEstateClassifieds.dto.FurnishedTypeDto;
import ru.otus.realEstateClassifieds.services.FurnishedTypeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер взаимодействия с сущностями степеней мебелирования")
public class FurnishedTypeController {
    private final FurnishedTypeService furnishedTypeService;

    @GetMapping("/furnished-types")
    @Operation(summary = "Поиск всех степеней мебелирования")
    public List<FurnishedTypeDto> getAllFurnishedTypes() {
        return furnishedTypeService.findAll();
    }

    @PostMapping("/furnished-types")
    @Operation(summary = "Создание новой степени мебелирования")
    public void createFurnishedType(@Valid @RequestBody FurnishedTypeDto furnishedType) {
        furnishedTypeService.create(furnishedType);
    }

    @PatchMapping("/furnished-types")
    @Operation(summary = "Обновление существующей степени мебелирования")
    public FurnishedTypeDto updateFurnishedType(@Valid @RequestBody FurnishedTypeDto furnishedType) {
        return furnishedTypeService.update(furnishedType);
    }

    @DeleteMapping("/furnished-types/{id}")
    @Operation(summary = "Удаление степени мебелирования")
    public void deleteFurnishedType(@PathVariable long id) {
        furnishedTypeService.deleteById(id);
    }
}
