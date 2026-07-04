package com.blog.be.post.application;

import com.blog.be.post.domain.Post;
import com.blog.be.post.domain.PostErrorCode;
import com.blog.be.post.domain.PostException;
import com.blog.be.post.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<Post> findByCategoryId(Long categoryId) {
        return postRepository.findAllByCategoryId(categoryId);
    }

    public List<Post> findBySeriesId(Long seriesId) {
        return postRepository.findAllBySeriesId(seriesId);
    }
}
