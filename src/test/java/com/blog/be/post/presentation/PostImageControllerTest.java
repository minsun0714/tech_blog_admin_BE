package com.blog.be.post.presentation;

import com.blog.be.post.application.PostImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.blog.be.support.MockMvcUtils.apiKey;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(PostImageController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class PostImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostImageService postImageService;

    @Test
    @DisplayName("게시글 이미지를 업로드한다.")
    void uploadPostImage() throws Exception {
        // given
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "image".getBytes()
        );

        given(postImageService.uploadAndGetImageUrl(
                any()
        )).willReturn("https://test.com/image.png");

        // when & then
        mockMvc.perform(
                        multipart("/api/images")
                                .file(image)
                                .with(apiKey())
                )
                .andExpect(status().isOk())
                .andDo(document("post-image/upload"));
    }
}