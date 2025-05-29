package ru.otus.realEstateClassifieds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.realEstateClassifieds.models.PropertyObject;

public interface PropertyObjectRepository extends JpaRepository<PropertyObject, Long> {

}