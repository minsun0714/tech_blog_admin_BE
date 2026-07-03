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
	public List<PostImageJpaEntity> findAllByPostId(Long postId) {
		return postImageJpaRepository.findAllByPostId(postId);
	}

	@Override
	public List<String> deleteAllByPostId(Long postId) {
		return postImageJpaRepository.deleteAllByPostId(postId)
				.stream().map(PostImageJpaEntity::getS3Key)
				.toList();
	}

	@Override
	public String saveFile(Long postId, String originalFilename, boolean isThumbnail) {

		PostImageJpaEntity postImageJpaEntity = postImageJpaRepository.save(PostImageJpaEntity.create(postId, originalFilename, isThumbnail));

		return postImageJpaEntity.getS3Key();
	}

	@Override
	public String deleteById(Long imageId) {
		String s3Key = postImageJpaRepository.findById(imageId)
						.map(PostImageJpaEntity::getS3Key)
						.orElseThrow(() -> new PostException(PostErrorCode.POST_IMAGE_NOT_FOUND));
		postImageJpaRepository.deleteByS3Key(s3Key);

		return s3Key;
	}
}
