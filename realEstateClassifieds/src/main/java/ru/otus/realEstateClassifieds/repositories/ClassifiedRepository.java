package ru.otus.realEstateClassifieds.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.realEstateClassifieds.models.Classified;

import java.util.List;
import java.util.Optional;

public interface ClassifiedRepository extends
        JpaRepository<Classified, Long>,
        JpaSpecificationExecutor<Classified> {

    @EntityGraph("classified-entity-graph")
    Optional<Classified> findById(long id);

    @Nonnull
    @EntityGraph("classified-entity-graph")
    List<Classified> findAll();

    @Nonnull
    @EntityGraph("classified-entity-graph")
    List<Classified> findAll(Specification specification);
}