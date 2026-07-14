package com.blog.be.comment.application;

import com.blog.be.comment.domain.CommentErrorCode;
import com.blog.be.comment.domain.CommentException;
import com.blog.be.comment.domain.CommentRepository;
import com.blog.be.comment.infrastructure.persistence.CommentJpaEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentCommandService {

    private final CommentRepository commentRepository;

    // 댓글 달기
    public void createRootComment(Long postId, String author, String password, String content) {
        CommentJpaEntity comment = CommentJpaEntity.createRoot(postId, author, password, content);

        commentRepository.save(comment);
    }

    // 대댓글 작성
    public void createReply(Long parentId, Long postId, String author, String password, String content) {
        Long parentCommentId = commentRepository.findById(parentId)
                .map(CommentJpaEntity::getId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.PARENT_COMMENT_NOT_FOUND));

        CommentJpaEntity newReply = CommentJpaEntity.createReply(postId, parentCommentId, author, password, content);
        commentRepository.save(newReply);
    }

    // 댓글 수정
    public void updateComment(Long id, String password, String newContent) {
        CommentJpaEntity comment = getComment(id);

        if (!comment.isPasswordMatch(password)) {
            throw new CommentException(CommentErrorCode.INVALID_PASSWORD);
        }

        comment.changeContent(newContent);
    }

    // 댓글 삭제
    public void deleteComment(Long id, String password) {
        CommentJpaEntity comment = getComment(id);

        if (!comment.isPasswordMatch(password)) {
            throw new CommentException(CommentErrorCode.INVALID_PASSWORD);
        }

        commentRepository.delete(comment);
    }

    private CommentJpaEntity getComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
    }
}
