package ru.otus.realEstateClassifieds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.realEstateClassifieds.models.FurnishedType;

public interface FurnishedTypeRepository extends JpaRepository<FurnishedType, Long> {

}
