package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(value = "book-entity-graph")
    Optional<Book> findById(long id);

    @Nonnull
    @EntityGraph(value = "book-entity-graph")
    @PostFilter("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    List<Book> findAll();
}
