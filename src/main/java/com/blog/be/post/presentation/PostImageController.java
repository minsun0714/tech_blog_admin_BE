package com.blog.be.post.presentation;

import com.blog.be.post.application.PostImageFacade;
import com.blog.be.post.presentation.dto.PostImageResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/posts/{postId}/images")
@RequiredArgsConstructor
public class PostImageController {

	private final PostImageFacade postImageFacade;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PostImageResponse> uploadPostImage(
			@PathVariable Long postId,
			@RequestPart MultipartFile image,
			@RequestParam boolean isThumbnail
	) {
		String imageUrl = postImageFacade.uploadAndGetImageUrl(
				postId,
				image,
				isThumbnail
		);
		return ResponseEntity.ok().body(PostImageResponse.of(imageUrl));
	}

	@DeleteMapping
	public ResponseEntity<Void> deletePostImagesByPostId(
		@PathVariable Long postId
	) {
		postImageFacade.deletePostImagesByPostId(postId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{imageId}")
	public ResponseEntity<Void> deletePostImage(
		@PathVariable Long imageId
	) {
		postImageFacade.deletePostImage(imageId);
		return ResponseEntity.ok().build();
	}

}
