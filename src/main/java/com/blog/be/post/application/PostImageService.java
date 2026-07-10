package com.blog.be.post.application;

import java.util.List;

import com.blog.be.post.application.dto.DeletedPostImage;
import com.blog.be.post.infrastructure.persistence.PostImageJpaEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.blog.be.post.domain.PostImageRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostImageService {

	@Value("${cloud.aws.s3.base-url")
	private String s3BaseUrl;

	private final ImageStorage imageStorage;

	public String uploadAndGetImageUrl(MultipartFile file) {
		String s3Key = imageStorage.upload(file);
		return String.join("/", s3BaseUrl, s3Key);
	}
}
