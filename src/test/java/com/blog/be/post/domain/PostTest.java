package com.blog.be.post.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostTest {

    @Test
    @DisplayName("공개 게시글을 생성한다.")
    void publish() {
        // when
        Post post = Post.publish(
                "제목",
                "내용",
                Set.of(),
                1L,
                1L,
                PublishStatus.PUBLISHED
        );

        // then
        assertThat(post.getPublishStatus()).isEqualTo(PublishStatus.PUBLISHED);
    }

    @Test
    @DisplayName("비공개 게시글을 생성한다.")
    void draft() {
        // when
        Post post = Post.publish(
                "제목",
                "내용",
                Set.of(),
                1L,
                1L,
                PublishStatus.DRAFTED
        );

        // then
        assertThat(post.getPublishStatus()).isEqualTo(PublishStatus.DRAFTED);
    }

    @Test
    @DisplayName("제목을 변경한다.")
    void changeTitle() {
        // given
        Post post = createPost();

        // when
        post.changeTitle("새 제목");

        // then
        assertThat(post.getTitle()).isEqualTo("새 제목");
    }

    @Test
    @DisplayName("내용을 변경한다.")
    void changeContent() {
        // given
        Post post = createPost();

        // when
        post.changeContent("새 내용");

        // then
        assertThat(post.getContent()).isEqualTo("새 내용");
    }

    @Test
    @DisplayName("태그를 추가한다.")
    void addTag() {
        // given
        Post post = createPost();

        // when
        post.addTag(1L);

        // then
        assertThat(post.getTagIds()).contains(1L);
    }

    @Test
    @DisplayName("태그를 삭제한다.")
    void removeTag() {
        // given
        Long tagId = 1L;

        Post post = Post.publish(
                "제목",
                "내용",
                Set.of(tagId),
                1L,
                1L,
                PublishStatus.PUBLISHED
        );

        // when
        post.removeTag(tagId);

        // then
        assertThat(post.getTagIds()).isEmpty();
    }

    @Test
    @DisplayName("카테고리를 변경한다.")
    void changeCategory() {
        // given
        Post post = createPost();

        Long categoryId = 10L;

        // when
        post.changeCategory(categoryId);

        // then
        assertThat(post.getCategoryId()).isEqualTo(categoryId);
    }

    @Test
    @DisplayName("시리즈를 변경한다.")
    void changeSeries() {
        // given
        Post post = createPost();

        Long seriesId = 10L;

        // when
        post.changeSeries(seriesId);

        // then
        assertThat(post.getSeriesId()).isEqualTo(seriesId);
    }

    @Test
    @DisplayName("게시글 정보를 한번에 변경한다.")
    void change() {
        // given
        Post post = createPost();

        Set<Long> tagIds = Set.of(1L, 2L);

        // when
        post.change(
                "새 제목",
                "새 내용",
                tagIds,
                10L,
                20L,
                PublishStatus.DRAFTED
        );

        // then
        assertThat(post.getTitle()).isEqualTo("새 제목");
        assertThat(post.getContent()).isEqualTo("새 내용");
        assertThat(post.getTagIds()).containsExactlyInAnyOrder(1L, 2L);
        assertThat(post.getCategoryId()).isEqualTo(10L);
        assertThat(post.getSeriesId()).isEqualTo(20L);
        assertThat(post.getPublishStatus()).isEqualTo(PublishStatus.DRAFTED);
    }

    @Test
    @DisplayName("태그를 변경한다.")
    void changeTags() {
        // given
        Post post = createPost();

        // when
        post.changeTags(Set.of(1L, 2L));

        // then
        assertThat(post.getTagIds())
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    @DisplayName("태그는 null일 수 없다.")
    void changeTagsNull() {
        // given
        Post post = createPost();

        // when & then
        assertThatThrownBy(() -> post.changeTags(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("변경 시 전달받은 컬렉션을 복사한다.")
    void defensiveCopy() {
        // given
        Post post = createPost();

        Set<Long> tags = new java.util.HashSet<>(Set.of(1L));

        // when
        post.changeTags(tags);

        tags.clear();

        // then
        assertThat(post.getTagIds()).containsExactly(1L);
    }

    private Post createPost() {
        return Post.publish(
                "제목",
                "내용",
                Set.of(),
                1L,
                1L,
                PublishStatus.PUBLISHED
        );
    }
}