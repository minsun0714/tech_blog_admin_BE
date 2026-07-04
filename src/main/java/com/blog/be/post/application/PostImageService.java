package com.blog.be.post.application;

import java.util.List;

import com.blog.be.post.application.dto.DeletedPostImage;
import com.blog.be.post.infrastructure.persistence.PostImageJpaEntity;
import org.springframework.stereotype.Service;

import com.blog.be.post.domain.PostImageRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostImageService {

	private final PostImageRepository postImageRepository;

	public void restore(Long postId, boolean isThumbnail, String s3Key) {
		PostImageJpaEntity postImageJpaEntity = PostImageJpaEntity.create(postId, s3Key, isThumbnail);
		postImageRepository.restore(postImageJpaEntity);
	}

	public void restoreAll(List<DeletedPostImage> deletedPostImageList) {
		List<PostImageJpaEntity> postImageJpaEntities = deletedPostImageList.stream()
						.map(deletedPostImage -> PostImageJpaEntity.create(deletedPostImage.postId(), deletedPostImage.s3Key(), deletedPostImage.isThumbnail()))
						.toList();

		postImageRepository.restoreAll(postImageJpaEntities);
	}

	public String uploadPostImage(Long postId, String s3Key, boolean isThumbnail) {
		return postImageRepository.saveFile(postId, s3Key, isThumbnail);
	}

	public List<DeletedPostImage> deletePostImagesByPostId(Long postId) {
		List<PostImageJpaEntity> postImageJpaEntities = postImageRepository.findAllByPostId(postId);
		postImageRepository.deleteAllByPostId(postId);
		return postImageJpaEntities.stream()
				.map(postImageJpaEntity ->
						DeletedPostImage.of(postImageJpaEntity.getPostId(), postImageJpaEntity.isThumbnail(), postImageJpaEntity.getS3Key()))
				.toList();
	}

	public DeletedPostImage deletePostImage(Long imageId) {
		PostImageJpaEntity postImageJpaEntity = postImageRepository.findById(imageId);
		postImageRepository.deleteById(imageId);
		return DeletedPostImage.of(postImageJpaEntity.getPostId(), postImageJpaEntity.isThumbnail(), postImageJpaEntity.getS3Key());
	}
}
