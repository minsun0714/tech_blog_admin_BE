package com.blog.be.post.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.be.post.application.PostImageService;
import com.blog.be.post.presentation.dto.PostImageUploadRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts/{postId}/images")
@RequiredArgsConstructor
public class PostImageController {

	private final PostImageService postImageService;

	@PostMapping
	public ResponseEntity<Void> uploadPostImage(
		@PathVariable Long postId,
		@RequestBody PostImageUploadRequest postImageUploadRequest
	) {
		postImageService.uploadPostImage(
			postId,
			postImageUploadRequest.multipartFile(),
			postImageUploadRequest.isThumbnail()
		);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping
	public ResponseEntity<Void> deletePostImage(
		@PathVariable Long postId
	) {
		postImageService.deletePostImagesByPostId(postId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{originalFilename}")
	public ResponseEntity<Void> deletePostImage(
		@PathVariable String originalFilename
	) {
		postImageService.deletePostImage(originalFilename);
		return ResponseEntity.ok().build();
	}

}
