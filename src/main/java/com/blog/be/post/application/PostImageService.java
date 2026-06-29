package com.blog.be.post.application;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.blog.be.post.domain.Post;
import com.blog.be.post.domain.PostErrorCode;
import com.blog.be.post.domain.PostException;
import com.blog.be.post.domain.PostImageRepository;
import com.blog.be.post.domain.PostRepository;
import com.blog.be.post.domain.PostTagRepository;
import com.blog.be.post.domain.image.PostImage;
import com.blog.be.tag.application.TagCommandService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostImageService {

	private final PostImageRepository postImageRepository;

	public String uploadPostImage(Long postId, MultipartFile multipartFile, boolean isThumbnail) {
		return postImageRepository.saveFile(postId, multipartFile, isThumbnail);
	}

	public void deletePostImagesByPostId(Long postId) {
		postImageRepository.deleteAllByPostId(postId);
	}

	public void deletePostImage(String originalFilename) {
		postImageRepository.deleteFile(originalFilename);
	}
}
