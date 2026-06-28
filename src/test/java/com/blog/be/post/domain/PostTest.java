package com.blog.be.post.domain;

import com.blog.be.category.domain.CategoryId;
import com.blog.be.post.domain.image.PostImage;
import com.blog.be.post.domain.image.PostImageId;
import com.blog.be.series.domain.SeriesId;
import com.blog.be.tag.domain.TagId;
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
                new CategoryId(1L),
                new SeriesId(1L)
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
                new CategoryId(1L),
                new SeriesId(1L)
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
        post.addImage(new PostImageId(1L), false);

        // then
        assertThat(post.getPostImages()).hasSize(1);
    }

    @Test
    @DisplayName("중복 이미지는 추가할 수 없다.")
    void addDuplicatedImage() {
        // given
        Post post = createPost();

        PostImageId imageId = new PostImageId(1L);

        post.addImage(imageId, false);

        // when & then
        assertThatThrownBy(() -> post.addImage(imageId, false))
                .isInstanceOf(PostException.class);
    }

    @Test
    @DisplayName("이미지를 삭제한다.")
    void removeImage() {
        // given
        PostImageId imageId = new PostImageId(1L);

        Post post = Post.publish(
                "제목",
                "내용",
                List.of(PostImage.create(imageId, false)),
                Set.of(),
                new CategoryId(1L),
                new SeriesId(1L)
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
                post.removeImage(new PostImageId(100L)))
                .isInstanceOf(PostException.class);
    }

    @Test
    @DisplayName("썸네일을 변경한다.")
    void changeThumbnailImage() {
        // given
        PostImageId image1 = new PostImageId(1L);
        PostImageId image2 = new PostImageId(2L);

        Post post = Post.publish(
                "제목",
                "내용",
                List.of(
                        PostImage.create(image1, true),
                        PostImage.create(image2, false)
                ),
                Set.of(),
                new CategoryId(1L),
                new SeriesId(1L)
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
        post.addTag(new TagId(1L));

        // then
        assertThat(post.getTagIds()).contains(new TagId(1L));
    }

    @Test
    @DisplayName("태그를 삭제한다.")
    void removeTag() {
        // given
        TagId tagId = new TagId(1L);

        Post post = Post.publish(
                "제목",
                "내용",
                List.of(),
                Set.of(tagId),
                new CategoryId(1L),
                new SeriesId(1L)
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

        CategoryId categoryId = new CategoryId(10L);

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

        SeriesId seriesId = new SeriesId(10L);

        // when
        post.changeSeries(seriesId);

        // then
        assertThat(post.getSeriesId()).isEqualTo(seriesId);
    }

    private Post createPost() {
        return Post.publish(
                "제목",
                "내용",
                List.of(),
                Set.of(),
                new CategoryId(1L),
                new SeriesId(1L)
        );
    }
}