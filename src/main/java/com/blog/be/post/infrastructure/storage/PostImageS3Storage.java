package com.blog.be.post.infrastructure.storage;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
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
    public String upload(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        String s3Key = originalFilename + "." + randomUUID();

        try {
            amazonS3.putObject(bucket, s3Key, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return s3Key;
    }

    @Override
    public void deleteMany(List<String> s3KeyListToDelete) {
        amazonS3.deleteObjects(new DeleteObjectsRequest(bucket)
                .withKeys(
                        s3KeyListToDelete.stream()
                            .map(DeleteObjectsRequest.KeyVersion::new)
                                .toList()
                )
        );
    }

    @Override
    public void deleteOne(String S3Key) {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, S3Key));
        } catch (SdkClientException e) {
            throw new RuntimeException(e);
        }
    }
}
