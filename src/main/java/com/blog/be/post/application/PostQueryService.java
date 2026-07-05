package com.blog.be.post.application;

import com.blog.be.post.domain.Post;
import com.blog.be.post.domain.PostErrorCode;
import com.blog.be.post.domain.PostException;
import com.blog.be.post.domain.PostRepository;
import com.blog.be.post.infrastructure.repository.dto.PostTagName;
import com.blog.be.post.presentation.dto.PostResponse;
import com.blog.be.tag.TagQueryService;
import com.blog.be.tag.domain.TagRepository;
import com.blog.be.tag.infrastructure.persistence.TagJpaEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostQueryService {

    private final PostRepository postRepository;
    private final TagQueryService tagQueryService;
    private final TagRepository tagRepository;

    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
    }

    public PostResponse getOnePost(Long postId) {
        Post post = findById(postId);

        List<String> tagNames = tagRepository.findAllByIdIn(post.getTagIds())
                .stream().map(TagJpaEntity::getName)
                .toList();

        return PostResponse.of(post, tagNames);
    }

    public Page<PostResponse> getPagedPosts(
            Long categoryId,
            Long seriesId,
            Pageable pageable
    ) {
        Page<Post> posts = findAll(categoryId, seriesId, pageable);

        Set<Long> postIds = posts.stream()
                .map(Post::getPostId)
                .collect(Collectors.toSet());

        Map<Long, List<String>> tagNamesByPostId =
                tagQueryService.findTagNamesByPostIds(postIds);

        return posts.map(post ->
                PostResponse.of(
                        post,
                        tagNamesByPostId.getOrDefault(post.getPostId(), List.of())
                )
        );
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
