package com.blog.be.post.application;

import lombok.RequiredArgsConstructor;
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
        List<String> s3KeyList = postImageService.deletePostImagesByPostId(postId);
        imageStorage.deleteMany(s3KeyList);
    }

    public void deletePostImage(Long imageId) {
        String S3Key = postImageService.deletePostImage(imageId);

        imageStorage.deleteOne(S3Key);
    }
}