package com.blog.be.post.infrastructure.repository;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.blog.be.post.domain.PostImageRepository;
import com.blog.be.post.infrastructure.persistence.PostImageJpaEntity;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostImageRepositoryImpl implements PostImageRepository {

	private final PostImageJpaRepository postImageJpaRepository;
	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Override
	public List<PostImageJpaEntity> findAllByPostId(Long postId) {
		return postImageJpaRepository.findAllByPostId(postId);
	}

	@Override
	public void deleteAllByPostId(Long postId) {
		postImageJpaRepository.deleteAllByPostId(postId);
	}

	@Override
	public String saveFile(Long postId, MultipartFile multipartFile, boolean isThumbnail) {
		String originalFilename = multipartFile.getOriginalFilename();

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(multipartFile.getSize());
		metadata.setContentType(multipartFile.getContentType());

		try {
			amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		postImageJpaRepository.save(PostImageJpaEntity.create(postId, originalFilename, isThumbnail));

		return amazonS3.getUrl(bucket, originalFilename).toString();
	}

	@Override
	public void deleteFile(String originalFilename) {
		amazonS3.deleteObject(bucket, originalFilename);
	}
}
