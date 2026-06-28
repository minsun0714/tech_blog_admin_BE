package com.blog.be.post.application;

import com.blog.be.post.domain.Post;
import com.blog.be.post.domain.PostRepository;
import com.blog.be.post.domain.PostTagRepository;
import com.blog.be.post.domain.image.PostImage;
import com.blog.be.tag.application.TagCommandService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final TagCommandService tagCommandService;
    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;

    public void publishPost(
            String title,
            String content,
            List<PostImage> postImages,
            Set<String> tagNames,
            Long categoryId,
            Long seriesId
    ) {
        Set<Long> tagIds = tagCommandService.upsertAllAndGetIds(tagNames);

        Post post = Post.publish(
                title,
                content,
                postImages,
                tagIds,
                categoryId,
                seriesId
        );

        Long postId = postRepository.save(post).getPostId();

        postTagRepository.saveAll(postId, tagIds);
    }

    public void draftPost(
            String title,
            String content,
            List<PostImage> postImages,
            Set<String> tagNames,
            Long categoryId,
            Long seriesId
    ) {
        Set<Long> tagIds = tagCommandService.upsertAllAndGetIds(tagNames);

        Post post = Post.draft(
                title,
                content,
                postImages,
                tagIds,
                categoryId,
                seriesId
        );

        Long postId = postRepository.save(post).getPostId();

        postTagRepository.saveAll(postId, tagIds);
    }
}
