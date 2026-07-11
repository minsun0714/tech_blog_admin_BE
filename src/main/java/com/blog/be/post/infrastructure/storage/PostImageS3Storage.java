package com.blog.be.post.infrastructure.storage;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.blog.be.post.application.ImageStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.util.UUID.randomUUID;

@Repository
@RequiredArgsConstructor
public class PostImageS3Storage implements ImageStorage {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String upload(MultipartFile multipartFile, String postUuid) {
        String originalFilename = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        String s3Key = postUuid + "." + originalFilename + "." + randomUUID();

        try {
            amazonS3.putObject(bucket, s3Key, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return s3Key;
    }

    @Override
    public void deleteMany(String postUuid) {
        String prefix = postUuid + ".";

        List<String> keys = amazonS3.listObjects(bucket, prefix)
                .getObjectSummaries()
                .stream()
                .map(S3ObjectSummary::getKey)
                .toList();

        if (keys.isEmpty()) {
            return;
        }

        DeleteObjectsRequest request = new DeleteObjectsRequest(bucket)
                .withKeys(
                        keys.stream()
                                .map(DeleteObjectsRequest.KeyVersion::new)
                                .toList()
                );

        amazonS3.deleteObjects(request);
    }
}
