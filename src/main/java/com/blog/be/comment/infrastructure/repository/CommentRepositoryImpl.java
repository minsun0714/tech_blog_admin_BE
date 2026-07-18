package com.blog.be.comment.infrastructure.repository;

import com.blog.be.comment.domain.CommentRepository;
import com.blog.be.comment.infrastructure.persistence.CommentJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;


    @Override
    public CommentJpaEntity save(CommentJpaEntity comment) {
        return commentJpaRepository.save(comment);
    }

    @Override
    public Optional<CommentJpaEntity> findById(Long commentId) {
        return commentJpaRepository.findById(commentId);
    }

    @Override
    public List<CommentJpaEntity> findAllByPostId(Long postId) {
        return commentJpaRepository.findAll();
    }

    @Override
    public void delete(CommentJpaEntity comment) {
        commentJpaRepository.delete(comment);
    }

    @Override
    public boolean existsById(Long commentId) {
        return commentJpaRepository.existsById(commentId);
    }
}
