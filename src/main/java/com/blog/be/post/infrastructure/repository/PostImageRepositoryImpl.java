package com.blog.be.post.infrastructure.repository;

import java.util.List;

import com.blog.be.post.domain.PostErrorCode;
import com.blog.be.post.domain.PostException;
import org.springframework.stereotype.Repository;

import com.blog.be.post.domain.PostImageRepository;
import com.blog.be.post.infrastructure.persistence.PostImageJpaEntity;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostImageRepositoryImpl implements PostImageRepository {

	private final PostImageJpaRepository postImageJpaRepository;

	@Override
	public void restore(PostImageJpaEntity postImageJpaEntity) {
		postImageJpaRepository.save(postImageJpaEntity);
	}

	@Override
	public void restoreAll(List<PostImageJpaEntity> postImageJpaEntities) {
		postImageJpaRepository.saveAll(postImageJpaEntities);
	}

	@Override
	public PostImageJpaEntity findById(Long imageId) {
		return postImageJpaRepository.findById(imageId)
				.orElseThrow(() -> new PostException(PostErrorCode.POST_IMAGE_NOT_FOUND));
	}

	@Override
	public List<PostImageJpaEntity> findAllByPostId(Long postId) {
		return postImageJpaRepository.findAllByPostId(postId);
	}

	@Override
	public List<PostImageJpaEntity> deleteAllByPostId(Long postId) {
		return postImageJpaRepository.deleteAllByPostId(postId);
	}

	@Override
	public String saveFile(Long postId, String originalFilename, boolean isThumbnail) {

		PostImageJpaEntity postImageJpaEntity = postImageJpaRepository.save(PostImageJpaEntity.create(postId, originalFilename, isThumbnail));

		return postImageJpaEntity.getS3Key();
	}

	@Override
	public PostImageJpaEntity deleteById(Long postImageId) {
		PostImageJpaEntity postImageJpaEntity = postImageJpaRepository.findById(postImageId)
				.orElseThrow(() -> new PostException(PostErrorCode.POST_IMAGE_NOT_FOUND));
		postImageJpaRepository.deleteById(postImageId);
		return postImageJpaEntity;
	}

	@Override
	public PostImageJpaEntity delete(Long postImageId) {
		PostImageJpaEntity postImageJpaEntity = postImageJpaRepository.findById(postImageId)
				.orElseThrow(() -> new PostException(PostErrorCode.POST_IMAGE_NOT_FOUND));
		postImageJpaRepository.delete(postImageJpaEntity);
		return postImageJpaEntity;
	}
}
