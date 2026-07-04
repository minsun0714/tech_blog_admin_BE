package com.blog.be.post.domain;

import java.util.List;


import com.blog.be.post.infrastructure.persistence.PostImageJpaEntity;

public interface PostImageRepository {

	void restore(PostImageJpaEntity postImageJpaEntity);

	void restoreAll(List<PostImageJpaEntity> postImageJpaEntities);

	PostImageJpaEntity findById(Long imageId);

	List<PostImageJpaEntity> findAllByPostId(Long postId);

	List<PostImageJpaEntity> deleteAllByPostId(Long postId);

	String saveFile(Long postId, String s3Key, boolean isThumbnail);

	PostImageJpaEntity deleteById(Long imageId);

	PostImageJpaEntity delete(Long postImageId);
}
