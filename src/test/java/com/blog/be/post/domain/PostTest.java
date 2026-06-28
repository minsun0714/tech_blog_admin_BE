package com.blog.be.post.domain;

import com.blog.be.post.domain.image.PostImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
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
                List.of(),
                Set.of(),
                1L,
                1L
        );

        // then
        assertThat(post.getOpenStatus()).isEqualTo(OpenStatus.PUBLIC);
    }

    @Test
    @DisplayName("비공개 게시글을 생성한다.")
    void draft() {
        // when
        Post post = Post.draft(
                "제목",
                "내용",
                List.of(),
                Set.of(),
                1L,
                1L
        );

        // then
        assertThat(post.getOpenStatus()).isEqualTo(OpenStatus.PRIVATE);
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
    @DisplayName("이미지를 추가한다.")
    void addImage() {
        // given
        Post post = createPost();

        // when
        post.addImage(1L, false);

        // then
        assertThat(post.getPostImages()).hasSize(1);
    }

    @Test
    @DisplayName("중복 이미지는 추가할 수 없다.")
    void addDuplicatedImage() {
        // given
        Post post = createPost();

        Long imageId = 1L;

        post.addImage(imageId, false);

        // when & then
        assertThatThrownBy(() -> post.addImage(imageId, false))
                .isInstanceOf(PostException.class);
    }

    @Test
    @DisplayName("이미지를 삭제한다.")
    void removeImage() {
        // given
        Long imageId = 1L;

        Post post = Post.publish(
                "제목",
                "내용",
                List.of(PostImage.create(imageId, false)),
                Set.of(),
                1L,
                1L
        );

        // when
        post.removeImage(imageId);

        // then
        assertThat(post.getPostImages()).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 이미지는 삭제할 수 없다.")
    void removeImageNotFound() {
        // given
        Post post = createPost();

        // when & then
        assertThatThrownBy(() ->
                post.removeImage(100L))
                .isInstanceOf(PostException.class);
    }

    @Test
    @DisplayName("썸네일을 변경한다.")
    void changeThumbnailImage() {
        // given
        Long image1 = 1L;
        Long image2 = 2L;

        Post post = Post.publish(
                "제목",
                "내용",
                List.of(
                        PostImage.create(image1, true),
                        PostImage.create(image2, false)
                ),
                Set.of(),
                1L,
                1L
        );

        // when
        post.changeThumbnailImage(image2);

        // then
        assertThat(
                post.getPostImages()
                        .stream()
                        .filter(PostImage::isThumbnail)
        ).hasSize(1);

        assertThat(
                post.getPostImages()
                        .stream()
                        .filter(PostImage::isThumbnail)
                        .findFirst()
                        .orElseThrow()
                        .getId()
        ).isEqualTo(image2);
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
                List.of(),
                Set.of(tagId),
                1L,
                1L
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

        List<PostImage> images = List.of(
                PostImage.create(1L, true),
                PostImage.create(2L, false)
        );

        Set<Long> tagIds = Set.of(1L, 2L);

        // when
        post.change(
                "새 제목",
                "새 내용",
                images,
                tagIds,
                10L,
                20L
        );

        // then
        assertThat(post.getTitle()).isEqualTo("새 제목");
        assertThat(post.getContent()).isEqualTo("새 내용");
        assertThat(post.getPostImages()).containsExactlyElementsOf(images);
        assertThat(post.getTagIds()).containsExactlyInAnyOrder(1L, 2L);
        assertThat(post.getCategoryId()).isEqualTo(10L);
        assertThat(post.getSeriesId()).isEqualTo(20L);
    }

    @Test
    @DisplayName("게시글 이미지를 변경한다.")
    void changePostImages() {
        // given
        Post post = createPost();

        List<PostImage> images = List.of(
                PostImage.create(1L, true),
                PostImage.create(2L, false)
        );

        // when
        post.changePostImages(images);

        // then
        assertThat(post.getPostImages()).containsExactlyElementsOf(images);
    }

    @Test
    @DisplayName("게시글 이미지는 null일 수 없다.")
    void changePostImagesNull() {
        // given
        Post post = createPost();

        // when & then
        assertThatThrownBy(() -> post.changePostImages(null))
                .isInstanceOf(NullPointerException.class);
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

        List<PostImage> images = new java.util.ArrayList<>();
        images.add(PostImage.create(1L, true));

        Set<Long> tags = new java.util.HashSet<>(Set.of(1L));

        // when
        post.changePostImages(images);
        post.changeTags(tags);

        images.clear();
        tags.clear();

        // then
        assertThat(post.getPostImages()).hasSize(1);
        assertThat(post.getTagIds()).containsExactly(1L);
    }

    private Post createPost() {
        return Post.publish(
                "제목",
                "내용",
                List.of(),
                Set.of(),
                1L,
                1L
        );
    }
}