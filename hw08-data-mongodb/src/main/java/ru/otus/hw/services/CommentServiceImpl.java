package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<Comment> findByBookId(String id) {
        return commentRepository.findByBookId(id);
    }

    @Override
    public Comment insert(String text, String bookId) {
        return this.save(text, bookId);
    }

    @Override
    public Comment update(String id, String text, String bookId) {
        return this.save(id, text, bookId);
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    private Comment save(String id, String text, String bookId) {
        var comment = this.createComment(text, bookId);
        comment.setId(id);
        return commentRepository.save(comment);
    }

    private Comment save(String text, String bookId) {
        var comment = this.createComment(text, bookId);
        return commentRepository.save(comment);
    }

    private Comment createComment(String text, String bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        return new Comment(text, book);
    }
}
