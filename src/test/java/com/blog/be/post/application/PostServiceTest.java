package com.blog.be.post.application;

import com.blog.be.post.domain.*;
import com.blog.be.tag.application.TagCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private TagCommandService tagCommandService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostTagRepository postTagRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("게시글을 발행한다.")
    void publishPost() {
        // given
        Set<String> tagNames = Set.of("Spring", "DDD");
        Set<Long> tagIds = Set.of(1L, 2L);

        when(tagCommandService.upsertAllAndGetIds(tagNames))
                .thenReturn(tagIds);

        when(postRepository.save(any(Post.class)))
                .thenReturn(savedPost(OpenStatus.PUBLIC, tagIds));

        // when
        postService.publishPost(
                "제목",
                "내용",
                tagNames,
                1L,
                1L
        );

        // then
        verify(tagCommandService).upsertAllAndGetIds(tagNames);
        verify(postRepository).save(any(Post.class));
        verify(postTagRepository).saveAll(1L, tagIds);
    }

    @Test
    @DisplayName("게시글를 임시 저장한다.")
    void draftPost() {
        // given
        Set<String> tagNames = Set.of("Spring");
        Set<Long> tagIds = Set.of(1L);

        when(tagCommandService.upsertAllAndGetIds(tagNames))
                .thenReturn(tagIds);

        when(postRepository.save(any(Post.class)))
                .thenReturn(savedPost(OpenStatus.PRIVATE, tagIds));

        // when
        postService.draftPost(
                "제목",
                "내용",
                tagNames,
                1L,
                1L
        );

        // then
        verify(tagCommandService).upsertAllAndGetIds(tagNames);
        verify(postRepository).save(any(Post.class));
        verify(postTagRepository).saveAll(1L, tagIds);
    }

    @Test
    @DisplayName("게시글을 수정한다.")
    void updatePost() {
        // given
        Long postId = 1L;

        Set<String> tagNames = Set.of("Java");
        Set<Long> tagIds = Set.of(10L);

        Post post = savedPost(OpenStatus.PUBLIC, Set.of());

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(tagCommandService.upsertAllAndGetIds(tagNames))
                .thenReturn(tagIds);

        when(postRepository.save(any(Post.class)))
                .thenReturn(post);

        // when
        postService.updatePost(
                postId,
                "새 제목",
                "새 내용",
                tagNames,
                2L,
                3L
        );

        // then
        verify(postRepository).findById(postId);
        verify(postTagRepository).deleteAllByPostId(postId);
        verify(tagCommandService).upsertAllAndGetIds(tagNames);
        verify(postRepository).save(post);
        verify(postTagRepository).saveAll(postId, tagIds);

        assertThat(post.getTitle()).isEqualTo("새 제목");
        assertThat(post.getContent()).isEqualTo("새 내용");
        assertThat(post.getCategoryId()).isEqualTo(2L);
        assertThat(post.getSeriesId()).isEqualTo(3L);
        assertThat(post.getTagIds()).containsExactly(10L);
    }

    @Test
    @DisplayName("게시글을 삭제한다.")
    void deletePost() {
        // given
        Long postId = 1L;

        Post post = savedPost(OpenStatus.PUBLIC, Set.of());

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        // when
        postService.deletePost(postId);

        // then
        verify(postRepository).findById(postId);
        verify(postTagRepository).deleteAllByPostId(postId);
        verify(postRepository).delete(post);
    }

    @Test
    @DisplayName("존재하지 않는 게시글은 수정할 수 없다.")
    void updatePostNotFound() {
        // given
        when(postRepository.findById(1L))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                postService.updatePost(
                        1L,
                        "제목",
                        "내용",
                        Set.of(),
                        1L,
                        1L
                )
        )
                .isInstanceOf(PostException.class)
                .extracting("errorCode")
                .isEqualTo(PostErrorCode.POST_NOT_FOUND);

        verify(postRepository, never()).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 게시글은 삭제할 수 없다.")
    void deletePostNotFound() {
        // given
        when(postRepository.findById(1L))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                postService.deletePost(1L)
        )
                .isInstanceOf(PostException.class)
                .extracting("errorCode")
                .isEqualTo(PostErrorCode.POST_NOT_FOUND);

        verify(postRepository, never()).delete(any());
    }

    private Post savedPost(OpenStatus openStatus, Set<Long> tagIds) {
        return Post.restore(
                1L,
                "제목",
                "내용",
                openStatus,
                tagIds,
                1L,
                1L
        );
    }
}