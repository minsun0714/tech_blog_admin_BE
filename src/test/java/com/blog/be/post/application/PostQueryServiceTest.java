package com.blog.be.post.application;

import com.blog.be.post.domain.PublishStatus;
import com.blog.be.post.domain.Post;
import com.blog.be.post.domain.PostErrorCode;
import com.blog.be.post.domain.PostException;
import com.blog.be.post.domain.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostQueryServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostQueryService postQueryService;

    @Test
    @DisplayName("categoryId와 seriesId가 모두 있으면 예외가 발생한다.")
    void findAllWithCategoryIdAndSeriesId() {
        // given
        PageRequest pageable = PageRequest.of(0, 20);

        // when & then
        assertThatThrownBy(() -> postQueryService.findAllByOpenStatus(1L, 2L, null, PublishStatus.PUBLIC, pageable))
                .isInstanceOf(PostException.class)
                .extracting("errorCode")
                .isEqualTo(PostErrorCode.INVALID_POST_FILTER);
    }

    @Test
    @DisplayName("categoryId가 있으면 카테고리별 게시글을 조회한다.")
    void findAllByCategoryId() {
        // given
        PageRequest pageable = PageRequest.of(0, 20);
        Page<Post> postPage = new PageImpl<>(List.of(post()));

        when(postRepository.findAllByCategoryIdAndOpenStatus(1L, PublishStatus.PUBLIC, pageable))
                .thenReturn(postPage);

        // when
        postQueryService.findAllByOpenStatus(1L, null, null, PublishStatus.PUBLIC, pageable);

        // then
        verify(postRepository).findAllByCategoryIdAndOpenStatus(1L, PublishStatus.PUBLIC, pageable);
    }

    @Test
    @DisplayName("seriesId가 있으면 시리즈별 게시글을 조회한다.")
    void findAllBySeriesId() {
        // given
        PageRequest pageable = PageRequest.of(0, 20);
        Page<Post> postPage = new PageImpl<>(List.of(post()));

        when(postRepository.findAllBySeriesIdAndOpenStatus(2L, PublishStatus.PUBLIC, pageable))
                .thenReturn(postPage);

        // when
        postQueryService.findAllByOpenStatus(null, 2L, null, PublishStatus.PUBLIC, pageable);

        // then
        verify(postRepository).findAllBySeriesIdAndOpenStatus(2L, PublishStatus.PUBLIC, pageable);
    }

    @Test
    @DisplayName("필터가 없으면 전체 게시글을 페이지네이션 조회한다.")
    void findAll() {
        // given
        PageRequest pageable = PageRequest.of(0, 20);
        Page<Post> postPage = new PageImpl<>(List.of(post()));

        when(postRepository.findAllByOpenStatus(PublishStatus.PUBLIC, pageable))
                .thenReturn(postPage);

        // when
        postQueryService.findAllByOpenStatus(null, null, null, PublishStatus.PUBLIC, pageable);

        // then
        verify(postRepository).findAllByOpenStatus(PublishStatus.PUBLIC, pageable);
    }

    private Post post() {
        return Post.restore(
                1L,
                "제목",
                "내용",
                PublishStatus.PUBLIC,
                Set.of(1L),
                1L,
                1L
        );
    }
}
