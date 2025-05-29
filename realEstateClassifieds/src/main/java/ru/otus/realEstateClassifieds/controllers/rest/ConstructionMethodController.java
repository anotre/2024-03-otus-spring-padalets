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
import ru.otus.realEstateClassifieds.dto.ConstructionMethodDto;
import ru.otus.realEstateClassifieds.services.ConstructionMethodService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер взаимодействия с сущностями строительных технологий")
public class ConstructionMethodController {
    private final ConstructionMethodService constructionMethodService;

    @GetMapping("/construction-methods")
    @Operation(summary = "Поиск всех строительных технологий")
    public List<ConstructionMethodDto> getAllConstructionMethods() {
        return constructionMethodService.findAll();
    }

    @PostMapping("/construction-methods")
    @Operation(summary = "Создание новой строительной технологии")
    public void createConstructionMethod(@Valid @RequestBody ConstructionMethodDto constructionMethod) {
        constructionMethodService.create(constructionMethod);
    }

    @PatchMapping("/construction-methods")
    @Operation(summary = "Обновление существующей строительной технологии")
    public ConstructionMethodDto updateConstructionMethod(@Valid @RequestBody ConstructionMethodDto constructionMethod) {
        return constructionMethodService.update(constructionMethod);
    }

    @DeleteMapping("/construction-methods/{id}")
    @Operation(summary = "Удаление существующей строительной технологии")
    public void deleteConstructionMethod(@PathVariable long id) {
        this.constructionMethodService.deleteById(id);
    }
}
