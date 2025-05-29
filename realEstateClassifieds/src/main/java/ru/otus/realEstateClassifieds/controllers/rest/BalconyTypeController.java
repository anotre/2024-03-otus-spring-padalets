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
import ru.otus.realEstateClassifieds.dto.BalconyTypeDto;
import ru.otus.realEstateClassifieds.services.BalconyTypeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер взаимодействия с сущностями типов балконов")
public class BalconyTypeController {
    private final BalconyTypeService balconyTypeService;

    @GetMapping("/balcony-types")
    @Operation(summary = "Поиск всех типов балконов")
    public List<BalconyTypeDto> findAll() {
        return balconyTypeService.findAll();
    }

    @PostMapping("/balcony-types")
    @Operation(summary = "Создание нового типа балконов")
    public void createBalconyType(@Valid @RequestBody BalconyTypeDto balconyTypeDto) {
        balconyTypeService.create(balconyTypeDto);
    }

    @PatchMapping("/balcony-types")
    @Operation(summary = "Обновление существующего типа балконов")
    public BalconyTypeDto updateBalconyType(@Valid @RequestBody BalconyTypeDto balconyTypeDto) {
        return balconyTypeService.update(balconyTypeDto);
    }

    @DeleteMapping("/balcony-types/{id}")
    @Operation(summary = "Удаление существующего типа балконов")
    public void deleteBalconyType(@PathVariable long id) {
        balconyTypeService.deleteById(id);
    }
}
