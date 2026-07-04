package com.blog.be.post.domain.image;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Getter
public class PostImage {

    private Long id;

    private MultipartFile multipartFile;

    private boolean isThumbnail;

    private PostImage(Long postImageId, MultipartFile multipartFile, boolean isThumbnail) {
        this.id = postImageId;
        this.multipartFile = multipartFile;
        this.isThumbnail = isThumbnail;
    }

    public static PostImage create(MultipartFile multipartFile, boolean thumbnail) {
        return new PostImage(null, multipartFile, thumbnail);
    }

    public static PostImage restore(Long id, MultipartFile multipartFile, boolean thumbnail) {
        return new PostImage(id, multipartFile, thumbnail);
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
