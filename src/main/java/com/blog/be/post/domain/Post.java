package com.blog.be.post.domain;

import com.blog.be.post.domain.image.PostImage;
import lombok.Getter;

import java.util.*;

@Getter
public class Post {

    private Long postId;

    private String title;

    private String content;

    private OpenStatus openStatus;

    private List<PostImage> postImages;

    public List<PostImage> getPostImages() {
        return Collections.unmodifiableList(postImages);
    }

    private Set<Long> tagIds;

    public Set<Long> getTagIds() {
        return Collections.unmodifiableSet(tagIds);
    }

    private Long categoryId;

    private Long seriesId;

    private Long likeCount;

    private Post(String title, String content, OpenStatus openStatus, List<PostImage> postImages, Set<Long> tagIds, Long categoryId, Long seriesId) {
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

    private Post(
            Long postId,
            String title,
            String content,
            OpenStatus openStatus,
            List<PostImage> postImages,
            Set<Long> tagIds,
            Long categoryId,
            Long seriesId,
            Long likeCount
    ) {
        Objects.requireNonNull(postId);
        Objects.requireNonNull(title);
        Objects.requireNonNull(content);
        Objects.requireNonNull(openStatus);
        Objects.requireNonNull(postImages);
        Objects.requireNonNull(tagIds);
        Objects.requireNonNull(categoryId);
        Objects.requireNonNull(likeCount);

        this.postId = postId;
        this.title = title;
        this.content = content;
        this.openStatus = openStatus;
        this.postImages = new ArrayList<>(postImages);
        this.tagIds = new HashSet<>(tagIds);
        this.categoryId = categoryId;
        this.seriesId = seriesId;
        this.likeCount = likeCount;
    }

    public static Post restore(
            Long postId,
            String title,
            String content,
            OpenStatus openStatus,
            List<PostImage> postImages,
            Set<Long> tagIds,
            Long categoryId,
            Long seriesId,
            Long likeCount
    ) {
        return new Post(
                postId,
                title,
                content,
                openStatus,
                postImages,
                tagIds,
                categoryId,
                seriesId,
                likeCount
        );
    }

    public static Post publish(
            String title,
            String content,
            List<PostImage> postImages,
            Set<Long> tagIds,
            Long categoryId,
            Long seriesId
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
            Set<Long> tagIds,
            Long categoryId,
            Long seriesId
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

    public void change(
            String title,
            String content,
            List<PostImage> postImages,
            Set<Long> tagIds,
            Long categoryId,
            Long seriesId
    ){
        changeTitle(title);
        changeContent(content);
        changePostImages(postImages);
        changeCategory(categoryId);
        changeSeries(seriesId);
        changeTags(tagIds);
    }

    public void changeTitle(String title) {
        Objects.requireNonNull(title);

        this.title = title;
    }

    public void changeContent(String content) {
        Objects.requireNonNull(content);

        this.content = content;
    }

    public void addImage(Long postImageId, boolean isThumbnail) {
        Objects.requireNonNull(postImageId);

        validateDuplicatedPostImage(postImageId);

        if (isThumbnail) {
            postImages.forEach(PostImage::releaseThumbnailImage);
        }

        postImages.add(
                PostImage.create(postImageId, isThumbnail)
        );
    }

    private void validateDuplicatedPostImage(Long postImageId) {
        boolean isAlreadyAdded = postImages.stream().map(PostImage::getId).anyMatch(postImageId::equals);

        if (isAlreadyAdded) {
            throw new PostException(PostErrorCode.DUPLICATE_POST_IMAGE);
        }
    }

    public void removeImage(Long postImageId) {
        Objects.requireNonNull(postImageId);

        PostImage targetPostImage = getTargetPostImage(postImageId);

        postImages.remove(targetPostImage);
    }

    public void changeThumbnailImage(Long postImageId) {
        Objects.requireNonNull(postImageId);

        PostImage targetPostImage = getTargetPostImage(postImageId);

        postImages.forEach(PostImage::releaseThumbnailImage);

        targetPostImage.changeThumbnailImage();
    }

    private PostImage getTargetPostImage(Long postImageId) {
        return postImages.stream()
                .filter(postImage -> postImageId.equals(postImage.getId()))
                .findFirst()
                .orElseThrow(() -> new PostException(PostErrorCode.POST_IMAGE_NOT_FOUND));
    }

    public void addTag(Long tagId) {
        Objects.requireNonNull(tagId);

        tagIds.add(tagId);
    }

    public void removeTag(Long tagId) {
        Objects.requireNonNull(tagId);

        tagIds.remove(tagId);
    }

    public void changePostImages(List<PostImage> postImages) {
        Objects.requireNonNull(postImages);
        this.postImages = new ArrayList<>(postImages);
    }

    public void changeTags(Set<Long> tagIds) {
        Objects.requireNonNull(tagIds);
        this.tagIds = new HashSet<>(tagIds);
    }

    public void changeCategory (Long categoryId){
        Objects.requireNonNull(categoryId);

        this.categoryId = categoryId;
    }

    public void changeSeries(Long seriesId) {
        this.seriesId = seriesId;
    }



}
