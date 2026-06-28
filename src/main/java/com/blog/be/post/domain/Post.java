package com.blog.be.post.domain;

import com.blog.be.category.domain.CategoryId;
import com.blog.be.post.domain.image.PostImage;
import com.blog.be.post.domain.image.PostImageId;
import com.blog.be.series.domain.SeriesId;
import com.blog.be.tag.domain.TagId;
import jakarta.annotation.Nonnull;
import lombok.Getter;

import java.util.*;

public class Post {

    private PostId postId;

    @Getter
    private String title;

    @Getter
    private String content;

    @Getter
    private OpenStatus openStatus;

    private List<PostImage> postImages;

    public List<PostImage> getPostImages() {
        return Collections.unmodifiableList(postImages);
    }

    private Set<TagId> tagIds;

    public Set<TagId> getTagIds() {
        return Collections.unmodifiableSet(tagIds);
    }

    @Getter
    private CategoryId categoryId;

    @Getter
    private SeriesId seriesId;

    private Long likeCount;

    private Post(String title, String content, OpenStatus openStatus, List<PostImage> postImages, Set<TagId> tagIds, CategoryId categoryId, SeriesId seriesId) {
        Objects.requireNonNull(title);
        Objects.requireNonNull(content);
        Objects.requireNonNull(openStatus);
        Objects.requireNonNull(postImages);
        Objects.requireNonNull(categoryId);

        this.title = title;
        this.content = content;
        this.openStatus = openStatus;
        this.postImages = new ArrayList<>(postImages);
        this.tagIds = new HashSet<>(tagIds);
        this.categoryId = categoryId;
        this.seriesId = seriesId;
    }

    public static Post publish(
            String title,
            String content,
            List<PostImage> postImages,
            Set<TagId> tagIds,
            CategoryId categoryId,
            SeriesId seriesId
    ) {
        return new Post(
          title,
          content,
          OpenStatus.PUBLIC,
          postImages,
          tagIds,
          categoryId,
          seriesId
        );
    }

    public static Post draft(
            String title,
            String content,
            List<PostImage> postImages,
            Set<TagId> tagIds,
            CategoryId categoryId,
            SeriesId seriesId
    ) {
        return new Post(
                title,
                content,
                OpenStatus.PRIVATE,
                postImages,
                tagIds,
                categoryId,
                seriesId
        );
    }

    public void changeTitle(String title) {
        Objects.requireNonNull(title);

        this.title = title;
    }

    public void changeContent(String content) {
        Objects.requireNonNull(content);

        this.content = content;
    }

    public void addImage(PostImageId postImageId, boolean isThumbnail) {
        Objects.requireNonNull(postImageId);

        validateDuplicatedPostImage(postImageId);

        if (isThumbnail) {
            postImages.forEach(PostImage::releaseThumbnailImage);
        }

        postImages.add(
                PostImage.create(postImageId, isThumbnail)
        );
    }

    private void validateDuplicatedPostImage(PostImageId postImageId) {
        boolean isAlreadyAdded = postImages.stream().map(PostImage::getId).anyMatch(postImageId::equals);

        if (isAlreadyAdded) {
            throw new PostException(PostErrorCode.DUPLICATE_POST_IMAGE);
        }
    }

    public void removeImage(PostImageId postImageId) {
        Objects.requireNonNull(postImageId);

        PostImage targetPostImage = getTargetPostImage(postImageId);

        postImages.remove(targetPostImage);
    }

    public void changeThumbnailImage(PostImageId postImageId) {
        Objects.requireNonNull(postImageId);

        PostImage targetPostImage = getTargetPostImage(postImageId);

        postImages.forEach(PostImage::releaseThumbnailImage);

        targetPostImage.changeThumbnailImage();
    }

    private PostImage getTargetPostImage(PostImageId postImageId) {
        return postImages.stream()
                .filter(postImage -> postImageId.equals(postImage.getId()))
                .findFirst()
                .orElseThrow(() -> new PostException(PostErrorCode.POST_IMAGE_NOT_FOUND));
    }

    public void addTag(TagId tagId) {
        Objects.requireNonNull(tagId);

        tagIds.add(tagId);
    }

    public void removeTag(TagId tagId) {
        Objects.requireNonNull(tagId);

        tagIds.remove(tagId);
    }

    public void changeCategory (CategoryId categoryId){
        Objects.requireNonNull(categoryId);

        this.categoryId = categoryId;
    }

    public void changeSeries(SeriesId seriesId) {
        this.seriesId = seriesId;
    }

}
