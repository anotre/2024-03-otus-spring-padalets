package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Comment;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
    @Query("{ 'book.id' : ?0 }")
    Flux<Comment> findByBookId(String id);

    @DeleteQuery
    @Query(value = "{ 'book.id' : ?0 }", delete = true)
    Mono<Void> deleteByBookId(String id);
}
