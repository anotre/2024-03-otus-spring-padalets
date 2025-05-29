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
import ru.otus.realEstateClassifieds.dto.ClassifiedDto;
import ru.otus.realEstateClassifieds.dto.ClassifiedSearchDto;
import ru.otus.realEstateClassifieds.exceptions.EntityNotFoundException;
import ru.otus.realEstateClassifieds.services.ClassifiedService;
import ru.otus.realEstateClassifieds.services.ConstructionMethodService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер взаимодействия с сущностями объявлениями")
public class ClassifiedController {
    private final ClassifiedService classifiedService;

    private final ConstructionMethodService constructionMethodService;

    @GetMapping("/classifieds/{id}")
    @Operation(summary = "Поиск объявления по идентификатору")
    public ClassifiedDto findById(@PathVariable long id) {
        return this.classifiedService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Classified with id %d not found".formatted(id)));
    }

    @GetMapping("/classifieds/search")
    @Operation(summary = "Поиск всех объявлений удовлетворяющих переданных условиям")
    public List<ClassifiedDto> searchByParams(ClassifiedSearchDto searchDto) {
        return classifiedService.findByParams(searchDto);
    }

    @PostMapping("/classifieds")
    @Operation(summary = "Создание нового объявления")
    public void create(@Valid @RequestBody ClassifiedDto classified) {
        this.classifiedService.create(classified);
    }

    @PatchMapping("/classifieds")
    @Operation(summary = "Обновление существующего объявления")
    public ClassifiedDto update(@Valid @RequestBody ClassifiedDto classified) {
        return this.classifiedService.update(classified);
    }

    @DeleteMapping("/classifieds/{id}")
    @Operation(summary = "Удаление существующего объявления")
    public void deleteById(@PathVariable long id) {
        this.classifiedService.deleteById(id);
    }
}