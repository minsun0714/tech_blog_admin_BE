package com.blog.be.post.application;

import com.blog.be.post.application.dto.DeletedPostImage;
import com.blog.be.post.domain.PostImageRepository;
import com.blog.be.post.infrastructure.persistence.PostImageJpaEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostImageServiceTest {

    @Mock
    private PostImageRepository postImageRepository;

    @InjectMocks
    private PostImageService postImageService;

    @Test
    void restore() {

        postImageService.restore(1L, true, "a.png");

        verify(postImageRepository)
                .restore(argThat(entity ->
                        entity.getPostId().equals(1L)
                                && entity.getS3Key().equals("a.png")
                                && entity.isThumbnail()
                ));
    }

    @Test
    void restoreAll() {

        DeletedPostImage image1 =
                DeletedPostImage.of(1L, true, "a.png");

        DeletedPostImage image2 =
                DeletedPostImage.of(1L, false, "b.png");

        postImageService.restoreAll(List.of(image1, image2));

        verify(postImageRepository)
                .restoreAll(argThat(list ->
                        list.size() == 2
                                && list.get(0).getPostId().equals(1L)
                                && list.get(0).getS3Key().equals("a.png")
                                && list.get(0).isThumbnail()
                                && list.get(1).getPostId().equals(1L)
                                && list.get(1).getS3Key().equals("b.png")
                                && !list.get(1).isThumbnail()
                ));
    }

    @Test
    void uploadPostImage() {

        when(postImageRepository.saveFile(1L, "a.png", true))
                .thenReturn("https://image");

        String result =
                postImageService.uploadPostImage(1L, "a.png", true);

        assertEquals("https://image", result);

        verify(postImageRepository)
                .saveFile(1L, "a.png", true);
    }

    @Test
    void deletePostImagesByPostId() {

        PostImageJpaEntity entity1 =
                PostImageJpaEntity.create(1L, "a.png", true);

        PostImageJpaEntity entity2 =
                PostImageJpaEntity.create(1L, "b.png", false);

        when(postImageRepository.findAllByPostId(1L))
                .thenReturn(List.of(entity1, entity2));

        List<DeletedPostImage> result =
                postImageService.deletePostImagesByPostId(1L);

        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals(1L, result.get(0).postId()),
                () -> assertEquals("a.png", result.get(0).s3Key()),
                () -> assertTrue(result.get(0).isThumbnail()),
                () -> assertEquals(1L, result.get(1).postId()),
                () -> assertEquals("b.png", result.get(1).s3Key()),
                () -> assertFalse(result.get(1).isThumbnail())
        );

        verify(postImageRepository)
                .deleteAllByPostId(1L);
    }

    @Test
    void deletePostImage() {

        PostImageJpaEntity entity =
                PostImageJpaEntity.create(1L, "a.png", true);

        when(postImageRepository.findById(10L))
                .thenReturn(entity);

        DeletedPostImage result =
                postImageService.deletePostImage(10L);

        assertAll(
                () -> assertEquals(1L, result.postId()),
                () -> assertEquals("a.png", result.s3Key()),
                () -> assertTrue(result.isThumbnail())
        );

        verify(postImageRepository)
                .deleteById(10L);
    }
}