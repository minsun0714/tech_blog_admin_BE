package com.blog.be.post.application;

import com.blog.be.post.application.dto.DeletedPostImage;
import com.blog.be.post.domain.PostImageRepository;
import com.blog.be.post.infrastructure.persistence.PostImageJpaEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostImageServiceTest {

    @Mock
    private ImageStorage imageStorage;

    @InjectMocks
    private PostImageService postImageService;

    @Test
    void uploadPostImage() {

        MultipartFile file = mock(MultipartFile.class);
        String postUuid = UUID.randomUUID().toString();

        when(imageStorage.upload(file, postUuid))
                .thenReturn("https://image");

        String result =
                imageStorage.upload(file, postUuid);

        assertEquals("https://image", result);
    }
}