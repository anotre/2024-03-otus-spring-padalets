package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    @Query("{ 'id' : ?0 }")
    List<Comment> findByBookId(String id);

    @DeleteQuery
    @Query(value = "{ 'id' : ?0 }", delete = true)
    void deleteByBookId(String id);
}
