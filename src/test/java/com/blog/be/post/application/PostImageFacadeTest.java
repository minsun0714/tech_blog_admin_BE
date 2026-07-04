package com.blog.be.post.application;

import com.blog.be.post.application.dto.DeletedPostImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostImageFacadeTest {

    @Mock
    private ImageStorage imageStorage;

    @Mock
    private PostImageService postImageService;

    private PostImageFacade postImageFacade;

    @BeforeEach
    void setUp() {
        postImageFacade = new PostImageFacade(
                "https://bucket.s3.ap-northeast-2.amazonaws.com",
                imageStorage,
                postImageService
        );
    }

    @Test
    void upload_success() {
        MultipartFile file = mock(MultipartFile.class);

        when(imageStorage.upload(1L, file))
                .thenReturn("posts/1/image.png");

        String result = postImageFacade.uploadAndGetImageUrl(1L, file, true);

        assertEquals(
                "https://bucket.s3.ap-northeast-2.amazonaws.com/posts/1/image.png",
                result
        );

        verify(postImageService)
                .uploadPostImage(1L, "posts/1/image.png", true);

        verify(imageStorage, never()).deleteOne(any());
    }

    @Test
    void upload_fail_then_delete_s3() {
        MultipartFile file = mock(MultipartFile.class);

        when(imageStorage.upload(1L, file))
                .thenReturn("posts/1/image.png");

        doThrow(new RuntimeException())
                .when(postImageService)
                .uploadPostImage(anyLong(), anyString(), anyBoolean());

        assertThrows(RuntimeException.class,
                () -> postImageFacade.uploadAndGetImageUrl(1L, file, true));

        verify(imageStorage)
                .deleteOne("posts/1/image.png");
    }

    @Test
    void delete_all_success() {

        DeletedPostImage image1 =
                new DeletedPostImage(1L, true, "a.png");

        DeletedPostImage image2 =
                new DeletedPostImage(1L, false, "b.png");

        when(postImageService.deletePostImagesByPostId(1L))
                .thenReturn(List.of(image1, image2));

        postImageFacade.deletePostImagesByPostId(1L);

        verify(imageStorage)
                .deleteMany(List.of("a.png", "b.png"));

        verify(postImageService, never())
                .restoreAll(any());
    }

    @Test
    void delete_all_fail_then_restore() {

        DeletedPostImage image =
                new DeletedPostImage(1L, true, "a.png");

        when(postImageService.deletePostImagesByPostId(1L))
                .thenReturn(List.of(image));

        doThrow(new RuntimeException())
                .when(imageStorage)
                .deleteMany(any());

        assertThrows(RuntimeException.class,
                () -> postImageFacade.deletePostImagesByPostId(1L));

        verify(postImageService)
                .restoreAll(List.of(image));
    }

    @Test
    void delete_one_success() {

        DeletedPostImage image =
                new DeletedPostImage(1L, true, "a.png");

        when(postImageService.deletePostImage(10L))
                .thenReturn(image);

        postImageFacade.deletePostImage(10L);

        verify(imageStorage)
                .deleteOne("a.png");

        verify(postImageService, never())
                .restore(anyLong(), anyBoolean(), anyString());
    }

    @Test
    void delete_one_fail_then_restore() {

        DeletedPostImage image =
                new DeletedPostImage(1L, true, "a.png");

        when(postImageService.deletePostImage(10L))
                .thenReturn(image);

        doThrow(new RuntimeException())
                .when(imageStorage)
                .deleteOne("a.png");

        assertThrows(RuntimeException.class,
                () -> postImageFacade.deletePostImage(10L));

        verify(postImageService)
                .restore(1L, true, "a.png");
    }
}