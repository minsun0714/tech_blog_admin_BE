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
    public void createRootComment(Long postId, String content) {
        CommentJpaEntity comment = CommentJpaEntity.createRoot(postId, content);

        commentRepository.save(comment);
    }

    // 대댓글 작성
    public void createReply(Long parentId, Long postId, String content) {
        Long parentCommentId = commentRepository.findById(parentId)
                .map(CommentJpaEntity::getId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.PARENT_COMMENT_NOT_FOUND));

        CommentJpaEntity newReply = CommentJpaEntity.createReply(postId, parentCommentId, content);
        commentRepository.save(newReply);
    }

    // 댓글 수정
    public void updateComment(Long id, String newContent) {
        CommentJpaEntity comment = getComment(id);

        comment.changeContent(newContent);
    }

    // 댓글 삭제
    public void deleteComment(Long id) {
        CommentJpaEntity comment = getComment(id);

        commentRepository.delete(comment);
    }

    private CommentJpaEntity getComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
    }
}
