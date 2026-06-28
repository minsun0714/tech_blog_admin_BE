package com.blog.be.comment.domain;

import com.blog.be.post.domain.PostId;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Comment {

    private static String DELETED_COMMENT_MESSAGE = "삭제된 댓글입니다.";

    private CommentId id;

    private PostId postId;

    private CommentId parentCommentId;

    private String content;

    private boolean deleted;

    private Comment(PostId postId, CommentId parentCommentId, String content) {
        this.postId = postId;
        this.parentCommentId = parentCommentId;
        this.content = content;
    }

    public static Comment createRoot(PostId postId, String content) {
        Objects.requireNonNull(postId);
        validateContent(content);

        return new Comment(postId, null, content);
    }

    public static Comment createReply(
            PostId postId,
            CommentId parentCommentId,
            String content
    ) {
        Objects.requireNonNull(postId);
        Objects.requireNonNull(parentCommentId);
        validateContent(content);

        return new Comment(postId, parentCommentId, content);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment comment)) return false;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}