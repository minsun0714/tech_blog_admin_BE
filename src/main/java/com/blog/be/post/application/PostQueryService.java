package com.blog.be.post.application;

import com.blog.be.post.domain.Post;
import com.blog.be.post.domain.PostErrorCode;
import com.blog.be.post.domain.PostException;
import com.blog.be.post.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostQueryService {

    private final PostRepository postRepository;

    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
    }

    public Page<Post> findAll(Long categoryId, Long seriesId, Pageable pageable) {
        if (categoryId != null && seriesId != null) {
            throw new PostException(PostErrorCode.INVALID_POST_FILTER);
        }

        if (categoryId != null) {
            return postRepository.findAllByCategoryId(categoryId, pageable);
        }

        if (seriesId != null) {
            return postRepository.findAllBySeriesId(seriesId, pageable);
        }

        return postRepository.findAll(pageable);
    }
}
