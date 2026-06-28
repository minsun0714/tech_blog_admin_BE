package com.blog.be.comment.infrastructure;

import com.blog.be.category.domain.Category;
import com.blog.be.category.domain.CategoryId;
import com.blog.be.category.domain.CategoryRepository;
import com.blog.be.category.infrastructure.CategoryJpaRepository;
import com.blog.be.comment.domain.Comment;
import com.blog.be.comment.domain.CommentId;
import com.blog.be.comment.domain.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;


    @Override
    public Comment save(Comment comment) {
        return commentJpaRepository.save(comment);
    }

    @Override
    public Optional<Comment> findById(CommentId commentId) {
        return commentJpaRepository.findById(commentId.id());
    }

    @Override
    public void delete(Comment comment) {
        commentJpaRepository.delete(comment);
    }

    @Override
    public boolean existsById(CommentId commentId) {
        return commentJpaRepository.existsById(commentId.id());
    }
}
