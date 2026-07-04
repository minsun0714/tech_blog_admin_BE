package com.blog.be.comment.application;

import com.blog.be.comment.application.dto.CommentNode;
import com.blog.be.comment.domain.CommentRepository;
import com.blog.be.comment.infrastructure.persistence.CommentJpaEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentQueryService {

    private final CommentRepository commentRepository;

    public List<CommentNode> getAllCommentsByPostId(Long postId) {
        List<CommentJpaEntity> commentJpaEntities = commentRepository.findAll();

        Map<Long, CommentNode> commentMap = commentJpaEntities.stream()
                .collect(Collectors.toMap(
                        CommentJpaEntity::getId,
                        CommentNode::from
                ));

        commentJpaEntities
                .forEach(commentJpaEntity -> {
                    CommentNode current = commentMap.get(commentJpaEntity.getId());
                    CommentNode parent = commentMap.getOrDefault(commentJpaEntity.getParentCommentId(), null);

                    if (parent == null) return;
                    parent.children().add(current);
                });

        return commentMap.values()
                .stream()
                .filter(commentNode -> commentNode.parentCommentId() == null)
                .toList();
    }
}
