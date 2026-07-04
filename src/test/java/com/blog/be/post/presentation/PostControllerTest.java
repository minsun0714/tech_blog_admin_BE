package com.blog.be.post.presentation;

import com.blog.be.post.application.PostService;
import com.blog.be.post.presentation.dto.PostDraftRequest;
import com.blog.be.post.presentation.dto.PostPublishRequest;
import com.blog.be.post.presentation.dto.PostUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    @Test
    @DisplayName("게시글을 발행한다.")
    void publishPost() throws Exception {
        // given
        PostPublishRequest request = new PostPublishRequest(
                "제목",
                "내용",
                Set.of("Spring", "JPA"),
                1L,
                1L
        );

        // when & then
        mockMvc.perform(post("/api/posts/publish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(postService).publishPost(
                eq("제목"),
                eq("내용"),
                eq(Set.of("Spring", "JPA")),
                eq(1L),
                eq(1L)
        );
    }

    @Test
    @DisplayName("게시글을 임시 저장한다.")
    void draftPost() throws Exception {
        // given
        PostDraftRequest request = new PostDraftRequest(
                "제목",
                "내용",
                Set.of("Spring"),
                1L,
                1L
        );

        // when & then
        mockMvc.perform(post("/api/posts/draft")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(postService).draftPost(
                eq("제목"),
                eq("내용"),
                eq(Set.of("Spring")),
                eq(1L),
                eq(1L)
        );
    }

    @Test
    @DisplayName("게시글을 수정한다.")
    void updatePost() throws Exception {
        // given
        PostUpdateRequest request = new PostUpdateRequest(
                "수정 제목",
                "수정 내용",
                Set.of("DDD"),
                2L,
                3L
        );

        // when & then
        mockMvc.perform(patch("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(postService).updatePost(
                eq(1L),
                eq("수정 제목"),
                eq("수정 내용"),
                eq(Set.of("DDD")),
                eq(2L),
                eq(3L)
        );
    }

    @Test
    @DisplayName("게시글을 삭제한다.")
    void deletePost() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/posts/1"))
                .andExpect(status().isNoContent());

        verify(postService).deletePost(1L);
    }
}