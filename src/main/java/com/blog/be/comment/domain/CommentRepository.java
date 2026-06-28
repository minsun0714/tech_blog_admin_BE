package com.blog.be.comment.domain;

import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);

    Optional<Comment> findById(CommentId id);

    void delete(Comment comment);

    boolean existsById(CommentId id);
}
