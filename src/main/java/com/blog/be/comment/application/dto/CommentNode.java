package com.blog.be.comment.application.dto;

import com.blog.be.comment.infrastructure.persistence.CommentJpaEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public record CommentNode (
    Long id,
    Long postId,
    Long parentCommentId,
    String content,
    boolean deleted,
    List<CommentNode> children
) {
    public static CommentNode from(CommentJpaEntity commentJpaEntity) {
        return new CommentNode(
                commentJpaEntity.getId(),
                commentJpaEntity.getPostId(),
                commentJpaEntity.getParentCommentId(),
                commentJpaEntity.getContent(),
                commentJpaEntity.isDeleted(),
                new ArrayList<>()
        );
    }
}
