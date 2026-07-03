package com.blog.be.post.presentation;

import com.blog.be.post.application.PostImageFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostImageController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostImageFacade postImageFacade;

    @Test
    @DisplayName("게시글 이미지 업로드")
    void uploadPostImage() throws Exception {

        MockMultipartFile image = new MockMultipartFile(
                "image",              // @RequestPart 이름과 동일해야 함
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "image".getBytes()
        );

        mockMvc.perform(
                        multipart("/api/posts/{postId}/images", 1L)
                                .file(image)
                                .param("isThumbnail", "true")
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글의 모든 이미지 삭제")
    void deletePostImage() throws Exception {

        doNothing().when(postImageFacade)
                .deletePostImagesByPostId(anyLong());

        mockMvc.perform(
                        delete("/api/posts/{postId}/images", 1L)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 이미지 한 장 삭제")
    void deleteSinglePostImage() throws Exception {

        doNothing().when(postImageFacade)
                .deletePostImage(anyLong());

        mockMvc.perform(
                        delete("/api/posts/{postId}/images/{imageId}", 1L, 1L)
                )
                .andExpect(status().isOk());
    }
}