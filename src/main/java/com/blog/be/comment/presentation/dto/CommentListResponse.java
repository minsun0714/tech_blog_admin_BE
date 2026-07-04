package com.blog.be.comment.presentation.dto;

import com.blog.be.comment.application.dto.CommentNode;

import java.util.List;

public record CommentListResponse(
        List<CommentResponse> commentResponseList
) {
    public record CommentResponse(
        Long commentId,
        Long commentParentId,
        String content,
        List<CommentResponse> childrenCommentList
    ) {
        private static CommentResponse from(CommentNode comment) {
            return new CommentResponse(
                    comment.id(),
                    comment.parentCommentId(),
                    comment.content(),
                    comment.children().stream()
                            .map(CommentResponse::from)
                            .toList()
            );
        }
    }

    public static CommentListResponse from(List<CommentNode> commentNodes) {
        return new CommentListResponse(
                commentNodes.stream()
                        .map(CommentListResponse.CommentResponse::from)
                        .toList()
        );
    }
}
