package ru.otus.realEstateClassifieds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.realEstateClassifieds.models.ConstructionMethod;

public interface ConstructionMethodRepository extends JpaRepository<ConstructionMethod, Long> {

}