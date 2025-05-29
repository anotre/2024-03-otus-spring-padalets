package ru.otus.realEstateClassifieds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.realEstateClassifieds.models.Street;

import java.util.List;

public interface StreetRepository extends JpaRepository<Street, Long> {
    List<Street> findBySettlementId(long id);
}