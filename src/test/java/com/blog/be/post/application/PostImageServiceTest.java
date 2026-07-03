package com.blog.be.post.application;

import com.blog.be.post.domain.PostImageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostImageServiceTest {

    @Mock
    private PostImageRepository postImageRepository;

    @InjectMocks
    private PostImageService postImageService;

    @Test
    @DisplayName("게시글 이미지를 업로드한다.")
    void uploadPostImage() {
        // given
        when(postImageRepository.saveFile(1L, "posts/image.png", true))
                .thenReturn("posts/image.png");

        // when
        String s3Key = postImageService.uploadPostImage(
                1L,
                "posts/image.png",
                true
        );

        // then
        verify(postImageRepository)
                .saveFile(1L, "posts/image.png", true);

        assertThat(s3Key).isEqualTo("posts/image.png");
    }

    @Test
    @DisplayName("게시글의 모든 이미지를 삭제한다.")
    void deletePostImagesByPostId() {
        // when
        postImageService.deletePostImagesByPostId(1L);

        // then
        verify(postImageRepository)
                .deleteAllByPostId(1L);
    }

    @Test
    @DisplayName("게시글 이미지를 S3Key를 이용해 삭제한다.")
    void deletePostImage() {
        // when
        postImageService.deletePostImage(1L);

        // then
        verify(postImageRepository)
                .deleteById(1L);
    }
}