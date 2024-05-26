package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        var bookEntityGraph = em.getEntityGraph("book-entity-graph");

        return Optional.ofNullable(
                em.find(Book.class, id, Collections.singletonMap(FETCH.getKey(), bookEntityGraph))
        );
    }

    @Override
    public List<Book> findAll() {
        var bookEntityGraph = em.getEntityGraph("book-entity-graph");
        TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
        query.setHint(FETCH.getKey(), bookEntityGraph);
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            book.copy();
        }
        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {
        var book = this.findById(id);
        if (book.isPresent()) {
            em.remove(book.get());
            em.flush();
            em.detach(book.get());
        }
    }
}
