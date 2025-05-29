package ru.otus.realEstateClassifieds.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.realEstateClassifieds.models.Region;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {
    @EntityGraph(value = "regions-entity-graph")
    Optional<Region> findById(long id);

    List<Region> findByCountryId(long id);
}