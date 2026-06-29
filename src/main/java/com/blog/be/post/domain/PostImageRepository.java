package com.blog.be.post.domain;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.blog.be.post.infrastructure.persistence.PostImageJpaEntity;

public interface PostImageRepository {

	List<PostImageJpaEntity> findAllByPostId(Long postId);

	void deleteAllByPostId(Long postId);

	String saveFile(Long postId, MultipartFile multipartFile, boolean isThumbnail);

	void deleteFile(String originalFilename);
}
