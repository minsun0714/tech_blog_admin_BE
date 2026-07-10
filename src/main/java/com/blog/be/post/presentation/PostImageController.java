package com.blog.be.post.presentation;

import com.blog.be.post.application.PostImageService;
import com.blog.be.post.presentation.dto.PostImageResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class PostImageController {

	private final PostImageService postImageService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PostImageResponse> uploadPostImage(
			@RequestPart MultipartFile image
	) {
		String imageUrl = postImageService.uploadAndGetImageUrl(image);
		return ResponseEntity.ok().body(PostImageResponse.of(imageUrl));
	}

}
