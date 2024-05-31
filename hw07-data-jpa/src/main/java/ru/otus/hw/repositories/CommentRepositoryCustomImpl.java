package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public Comment update(Comment comment) {
        return em.merge(comment);
    }
}
