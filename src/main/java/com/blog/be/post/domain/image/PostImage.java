package com.blog.be.post.domain.image;

import lombok.Getter;

import java.util.Objects;

@Getter
public class PostImage {

    private Long id;

    private boolean isThumbnail;

    private PostImage(Long postImageId, boolean isThumbnail) {
        this.id = postImageId;
        this.isThumbnail = isThumbnail;
    }

    public static PostImage create(Long postImageId, boolean isThumbnail){
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
