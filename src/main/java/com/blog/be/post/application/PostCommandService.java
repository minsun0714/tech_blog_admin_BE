package com.blog.be.post.application;

import com.blog.be.post.domain.*;
import com.blog.be.tag.application.TagCommandService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostCommandService {

    private final TagCommandService tagCommandService;
    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;

    public void publishPost(
            String title,
            String content,
            Set<String> tagNames,
            Long categoryId,
            Long seriesId,
            String postUuid
    ) {
        Set<Long> tagIds = tagCommandService.upsertAllAndGetIds(tagNames);

        Post post = Post.publish(
                title,
                content,
                tagIds,
                categoryId,
                seriesId
        );

        Long postId = postRepository.save(post, postUuid).getPostId();

        postTagRepository.saveAll(postId, tagIds);
    }

    public void draftPost(
            String title,
            String content,
            Set<String> tagNames,
            Long categoryId,
            Long seriesId,
            String postUuid
    ) {
        Set<Long> tagIds = tagCommandService.upsertAllAndGetIds(tagNames);

        Post post = Post.draft(
                title,
                content,
                tagIds,
                categoryId,
                seriesId
        );

        Long postId = postRepository.save(post, postUuid).getPostId();

        postTagRepository.saveAll(postId, tagIds);
    }

    public void updatePost(
            Long postId,
            String newTitle,
            String newContent,
            Set<String> newTagNames,
            Long newCategoryId,
            Long newSeriesId
    ) {

        Post post = getPost(postId);

        postTagRepository.deleteAllByPostId(postId);

        // tag batch upsert (더 이상 게시물을 참조하지 않는 태그가 생겨도 삭제하지 않고 두기로 결정)
        Set<Long> upsertedTagIds = tagCommandService.upsertAllAndGetIds(newTagNames);

        post.change(
            newTitle,
            newContent,
            upsertedTagIds,
            newCategoryId,
            newSeriesId
        );

        postRepository.save(post);

        // tagNames와 postId로 PostTag 다시 생성
        postTagRepository.saveAll(postId, upsertedTagIds);
    }

    public void deletePost(Long postId) {
        Post post = getPost(postId);

        postTagRepository.deleteAllByPostId(postId);

        postRepository.delete(post);
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
    }
}
