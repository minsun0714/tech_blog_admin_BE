package com.blog.be.post.domain;

import java.util.List;


import com.blog.be.post.infrastructure.persistence.PostImageJpaEntity;

public interface PostImageRepository {

	List<PostImageJpaEntity> findAllByPostId(Long postId);

	List<String> deleteAllByPostId(Long postId);

	String saveFile(Long postId, String s3Key, boolean isThumbnail);

	String deleteById(Long imageId);
}
