package com.blog.be.comment.infrastructure.persistence;

import com.blog.be.comment.domain.CommentErrorCode;
import com.blog.be.comment.domain.CommentException;
import com.blog.be.common.infrastructure.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CommentJpaEntity extends BaseEntity {

    private static final String DELETED_COMMENT_MESSAGE = "삭제된 댓글입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column
    private Long parentCommentId;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean deleted;

    public static CommentJpaEntity createRoot(Long postId, String author, String password, String content) {
        Objects.requireNonNull(postId);
        Objects.requireNonNull(author);
        Objects.requireNonNull(password);
        validateContent(content);

        return CommentJpaEntity.builder()
                .postId(postId)
                .author(author)
                .password(password)
                .content(content)
                .deleted(false)
                .build();
    }

    public static CommentJpaEntity createReply(
            Long postId,
            Long parentCommentId,
            String author,
            String password,
            String content
    ) {
        Objects.requireNonNull(postId);
        Objects.requireNonNull(parentCommentId);
        Objects.requireNonNull(author);
        Objects.requireNonNull(password);
        validateContent(content);

        return CommentJpaEntity.builder()
                .postId(postId)
                .parentCommentId(parentCommentId)
                .author(author)
                .password(password)
                .content(content)
                .deleted(false)
                .build();
    }

    public void changeContent(String content) {
        validateContent(content);
        this.content = content;
    }

    public void delete() {
        this.deleted = true;
        this.content = DELETED_COMMENT_MESSAGE;
    }

    public boolean isRoot() {
        return parentCommentId == null;
    }

    private static void validateContent(String content) {
        Objects.requireNonNull(content);

        if (content.isBlank()) {
            throw new CommentException(CommentErrorCode.INVALID_COMMENT_CONTENT);
        }
    }
}