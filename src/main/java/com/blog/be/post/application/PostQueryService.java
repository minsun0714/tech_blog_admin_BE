package com.blog.be.post.application;

import com.blog.be.post.application.dto.PostCountResponse;
import com.blog.be.post.domain.*;
import com.blog.be.post.presentation.dto.PostResponse;
import com.blog.be.post.presentation.dto.PostResponseWithUuid;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostQueryService {

    private final PostRepository postRepository;
    private final TagQueryService tagQueryService;
    private final TagRepository tagRepository;

    public PostCountResponse getPostsCountByPublishStatus() {
        long publishedPostsCount = postRepository.countByPublishStatus(PublishStatus.PUBLISHED);
        long draftedPostsCount = postRepository.countByPublishStatus(PublishStatus.DRAFTED);

        return PostCountResponse.of(publishedPostsCount, draftedPostsCount);
    }

    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
    }

    public PostResponseWithUuid getOnePost(Long postId) {
        Post post = findById(postId);

        List<String> tagNames = tagRepository.findAllByIdIn(post.getTagIds())
                .stream().map(TagJpaEntity::getName)
                .toList();

        String postUuid = postRepository.findUuidById(postId);

        return PostResponseWithUuid.of(post, tagNames, postUuid);
    }

    public Page<PostResponse> getPagedPosts(
            PublishStatus publishStatus,
            Long categoryId,
            Long seriesId,
            Long tagId,
            Pageable pageable
    ) {
        Page<Post> posts = findAllByPublishStatus(categoryId, seriesId, tagId, publishStatus, pageable);

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

    public String getUuidByPostId(Long postId) {
        return postRepository.findUuidById(postId);
    }

    public Page<Post> findAllByPublishStatus(Long categoryId, Long seriesId, Long tagId, PublishStatus publishStatus, Pageable pageable) {
        long count = Stream.of(categoryId, seriesId, tagId)
                .filter(Objects::nonNull)
                .count();

        if (count > 1) {
            throw new PostException(PostErrorCode.INVALID_POST_FILTER);
        }

        if (categoryId != null) {
            return postRepository.findAllByCategoryIdAndPublishStatus(categoryId, publishStatus, pageable);
        }

        if (seriesId != null) {
            return postRepository.findAllBySeriesIdAndPublishStatus(seriesId, publishStatus, pageable);
        }

        if (tagId != null) {
            return postRepository.findAllByTagIdAndPublishStatus(tagId, publishStatus, pageable);
        }

        return postRepository.findAllByPublishStatus(publishStatus, pageable);
    }
}
