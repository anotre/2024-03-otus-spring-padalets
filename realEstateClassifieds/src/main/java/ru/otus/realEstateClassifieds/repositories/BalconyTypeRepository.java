package ru.otus.realEstateClassifieds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.realEstateClassifieds.models.BalconyType;

public interface BalconyTypeRepository extends JpaRepository<BalconyType, Long> {

}