package ru.otus.realEstateClassifieds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.realEstateClassifieds.models.Settlement;

import java.util.List;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
    List<Settlement> findByRegionId(long id);
}