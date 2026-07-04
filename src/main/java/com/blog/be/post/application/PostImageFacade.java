package com.blog.be.post.application;

import com.blog.be.post.application.dto.DeletedPostImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PostImageFacade {

    private final String s3BaseUrl;

    private final ImageStorage imageStorage;
    private final PostImageService postImageService;

    public PostImageFacade(
            @Value("${cloud.aws.s3.base-url}") String s3BaseUrl,
            ImageStorage imageStorage,
            PostImageService postImageService
    ) {
        this.s3BaseUrl = s3BaseUrl;
        this.imageStorage = imageStorage;
        this.postImageService = postImageService;
    }

    public String uploadAndGetImageUrl(Long postId,
                                       MultipartFile file,
                                       boolean thumbnail) {

        String s3Key = imageStorage.upload(postId, file);

        try {
            postImageService.uploadPostImage(postId, s3Key, thumbnail);
            return String.join("/", s3BaseUrl, s3Key);
        } catch (Exception e) {
            imageStorage.deleteOne(s3Key); // 실패 시 보상 로직
            throw e;
        }
    }

    public void deletePostImagesByPostId(Long postId) {
        List<DeletedPostImage> deletedPostImageList = postImageService.deletePostImagesByPostId(postId);
        List<String> s3KeyList = deletedPostImageList
                .stream()
                .map(DeletedPostImage::s3Key)
                .toList();

        try {
            imageStorage.deleteMany(s3KeyList);
        } catch (Exception e) {
            postImageService.restoreAll(deletedPostImageList);
            throw e;
        }
    }

    public void deletePostImage(Long imageId) {
        DeletedPostImage deletedPostImage = postImageService.deletePostImage(imageId);
        try {
            imageStorage.deleteOne(deletedPostImage.s3Key());
        } catch (Exception e) {
            postImageService.restore(deletedPostImage.postId(), deletedPostImage.isThumbnail(), deletedPostImage.s3Key());
            throw e;
        }
    }
}