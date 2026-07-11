package com.blog.be.post.presentation;

import com.blog.be.post.application.PostImageService;
import com.blog.be.post.presentation.dto.PostUuidResponse;
import com.blog.be.post.presentation.dto.PostImageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class PostImageController {

	private final PostImageService postImageService;

	@GetMapping("/uuid")
	public ResponseEntity<PostUuidResponse> getPostUuid() {
		return ResponseEntity.ok().body(PostUuidResponse.of(UUID.randomUUID().toString()));
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PostImageResponse> uploadPostImage(
			@RequestPart MultipartFile image,
			@RequestParam @NotNull String postUuid
	) {
		String imageUrl = postImageService.uploadAndGetImageUrl(image, postUuid);

		return ResponseEntity.ok(PostImageResponse.of(imageUrl));
	}

}
