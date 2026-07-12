package com.blog.be.post.application;

import com.blog.be.post.domain.*;
import com.blog.be.tag.application.TagCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostCommandServiceTest {

    @Mock
    private TagCommandService tagCommandService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostTagRepository postTagRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private PostCommandService postCommandService;

    @Test
    @DisplayName("게시글을 발행한다.")
    void publishPost() {
        // given
        String uuid = UUID.randomUUID().toString();
        Set<String> tagNames = Set.of("Spring", "DDD");
        Set<Long> tagIds = Set.of(1L, 2L);

        when(tagCommandService.upsertAllAndGetIds(tagNames))
                .thenReturn(tagIds);

        when(postRepository.save(any(Post.class), eq(uuid)))
                .thenReturn(savedPost(PublishStatus.PUBLIC, tagIds));

        // when
        postCommandService.publishPost(
                "제목",
                "내용",
                tagNames,
                1L,
                1L,
                uuid
        );

        // then
        verify(tagCommandService).upsertAllAndGetIds(tagNames);
        verify(postRepository).save(any(Post.class), eq(uuid));
        verify(postTagRepository).saveAll(1L, tagIds);
    }

    @Test
    @DisplayName("게시글를 임시 저장한다.")
    void draftPost() {
        // given
        String uuid = UUID.randomUUID().toString();
        Set<String> tagNames = Set.of("Spring");
        Set<Long> tagIds = Set.of(1L);

        when(tagCommandService.upsertAllAndGetIds(tagNames))
                .thenReturn(tagIds);

        when(postRepository.save(any(Post.class), eq(uuid)))
                .thenReturn(savedPost(PublishStatus.PRIVATE, tagIds));

        // when
        postCommandService.draftPost(
                "제목",
                "내용",
                tagNames,
                1L,
                1L,
                uuid
        );

        // then
        verify(tagCommandService).upsertAllAndGetIds(tagNames);
        verify(postRepository).save(any(Post.class), eq(uuid));
        verify(postTagRepository).saveAll(1L, tagIds);
    }

    @Test
    @DisplayName("게시글을 수정한다.")
    void updatePost() {
        // given
        Long postId = 1L;

        Set<String> tagNames = Set.of("Java");
        Set<Long> tagIds = Set.of(10L);

        Post post = savedPost(PublishStatus.PUBLIC, Set.of());

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        when(tagCommandService.upsertAllAndGetIds(tagNames))
                .thenReturn(tagIds);

        when(postRepository.save(any(Post.class)))
                .thenReturn(post);

        // when
        postCommandService.updatePost(
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

        Post post = savedPost(PublishStatus.PUBLIC, Set.of());

        when(postRepository.existsById(1L)).thenReturn(true);

        // when
        postCommandService.deletePost(postId);

        // then
        verify(postRepository).existsById(postId);
        verify(postTagRepository).deleteAllByPostId(postId);
        verify(postRepository).deleteById(postId);
    }

    @Test
    @DisplayName("존재하지 않는 게시글은 수정할 수 없다.")
    void updatePostNotFound() {
        // given
        when(postRepository.findById(1L))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                postCommandService.updatePost(
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
        when(postRepository.existsById(1L)).thenReturn(false);

        // when & then
        assertThatThrownBy(() ->
                postCommandService.deletePost(1L)
        )
                .isInstanceOf(PostException.class)
                .extracting("errorCode")
                .isEqualTo(PostErrorCode.POST_NOT_FOUND);

        verify(postRepository, never()).deleteById(anyLong());
    }

    private Post savedPost(PublishStatus publishStatus, Set<Long> tagIds) {
        return Post.restore(
                1L,
                "제목",
                "내용",
                publishStatus,
                tagIds,
                1L,
                1L
        );
    }
}