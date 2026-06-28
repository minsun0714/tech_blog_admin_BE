package com.blog.be.comment.domain;

import com.blog.be.comment.infrastructure.persistence.CommentJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CommentTest {

    @Test
    @DisplayName("루트 댓글을 생성한다.")
    void createRoot() {
        // given
        Long postId = 1L;

        // when
        CommentJpaEntity comment = CommentJpaEntity.createRoot(postId, "첫 번째 댓글");

        // then
        assertThat(comment.isRoot()).isTrue();
        assertThat(comment.isDeleted()).isFalse();
        assertThat(comment.getPostId()).isEqualTo(postId);
        assertThat(comment.getParentCommentId()).isNull();
        assertThat(comment.getContent()).isEqualTo("첫 번째 댓글");
    }

    @Test
    @DisplayName("대댓글을 생성한다.")
    void createReply() {
        // given
        Long postId = 1L;
        Long parentCommentId = 10L;

        // when
        CommentJpaEntity comment = CommentJpaEntity.createReply(
                postId,
                parentCommentId,
                "대댓글입니다."
        );

        // then
        assertThat(comment.isRoot()).isFalse();
        assertThat(comment.getParentCommentId()).isEqualTo(parentCommentId);
        assertThat(comment.getPostId()).isEqualTo(postId);
    }

    @Test
    @DisplayName("댓글 내용을 수정한다.")
    void changeContent() {
        // given
        CommentJpaEntity comment = CommentJpaEntity.createRoot(
                1L,
                "기존 댓글"
        );

        // when
        comment.changeContent("수정된 댓글");

        // then
        assertThat(comment.getContent()).isEqualTo("수정된 댓글");
    }

    @Test
    @DisplayName("댓글를 삭제한다.")
    void delete() {
        // given
        CommentJpaEntity comment = CommentJpaEntity.createRoot(
                1L,
                "삭제할 댓글"
        );

        // when
        comment.delete();

        // then
        assertThat(comment.isDeleted()).isTrue();
        assertThat(comment.getContent()).isEqualTo("삭제된 댓글입니다.");
    }

    @Test
    @DisplayName("루트 댓글 여부를 확인한다.")
    void isRoot() {
        // given
        CommentJpaEntity root = CommentJpaEntity.createRoot(
                1L,
                "루트"
        );

        CommentJpaEntity reply = CommentJpaEntity.createReply(
                1L,
                1L,
                "답글"
        );

        // then
        assertThat(root.isRoot()).isTrue();
        assertThat(reply.isRoot()).isFalse();
    }
}