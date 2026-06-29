package com.blog.be.post.presentation.dto;

import org.springframework.web.multipart.MultipartFile;

public record PostImageUploadRequest(
	MultipartFile multipartFile,
	boolean isThumbnail
) {
}
