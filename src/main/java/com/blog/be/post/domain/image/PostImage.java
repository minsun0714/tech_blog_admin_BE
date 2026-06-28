package com.blog.be.post.domain.image;

import lombok.Getter;

import java.util.Objects;

@Getter
public class PostImage {

    private PostImageId id;

    private boolean isThumbnail;

    private PostImage(PostImageId postImageId, boolean isThumbnail) {
        this.id = postImageId;
        this.isThumbnail = isThumbnail;
    }

    public static PostImage create(PostImageId postImageId, boolean isThumbnail){
        return new PostImage(postImageId, isThumbnail);
    }

    public void changeThumbnailImage() {
        this.isThumbnail = true;
    }

    public void releaseThumbnailImage() {
        this.isThumbnail = false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PostImage postImage = (PostImage) o;
        return Objects.equals(id, postImage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
