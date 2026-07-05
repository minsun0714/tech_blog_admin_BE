package com.blog.be.post.presentation;

import com.blog.be.post.application.PostCommandService;
import com.blog.be.post.application.PostQueryService;
import com.blog.be.post.domain.PostErrorCode;
import com.blog.be.post.domain.PostException;
import com.blog.be.post.domain.Post;
import com.blog.be.post.presentation.dto.PostDraftRequest;
import com.blog.be.post.presentation.dto.PostPublishRequest;
import com.blog.be.post.presentation.dto.PostUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static com.blog.be.support.MockMvcUtils.apiKey;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostQueryService postQueryService;

    @MockitoBean
    private PostCommandService postCommandService;

    @Test
    @DisplayName("개별 게시물을 조회한다.")
    void getOnPost() throws Exception {
        // given
        given(postQueryService.findById(1L))
                .willReturn(Post.publish(
                        "제목",
                        "내용",
                        Set.of(1L, 2L),
                        1L,
                        1L
                ));

        // when & then
        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andDo(document("post/get-detail"));
    }

    @Test
    @DisplayName("카테고리 필터로 게시글을 조회한다.")
    void getAllPostsByCategoryId() throws Exception {
        // given
        given(postQueryService.findAll(eq(1L), eq(null), any()))
                .willReturn(new PageImpl<>(List.of()));

        // when & then
        mockMvc.perform(get("/api/posts")
                        .param("categoryId", "1"))
                .andExpect(status().isOk());

        verify(postQueryService)
                .findAll(eq(1L), eq(null), any());
    }

    @Test
    @DisplayName("시리즈 필터로 게시글을 조회한다.")
    void getAllPostsBySeriesId() throws Exception {
        // given
        given(postQueryService.findAll(eq(null), eq(2L), any()))
                .willReturn(new PageImpl<>(List.of()));

        // when & then
        mockMvc.perform(get("/api/posts")
                        .param("seriesId", "2"))
                .andExpect(status().isOk());

        verify(postQueryService)
                .findAll(eq(null), eq(2L), any());
    }

    @Test
    @DisplayName("필터가 없으면 전체 게시글을 페이지네이션 조회한다.")
    void getAllPosts() throws Exception {
        // given
        given(postQueryService.findAll(eq(null), eq(null), any()))
                .willReturn(new PageImpl<>(List.of(
                        Post.publish(
                                "제목",
                                "내용",
                                Set.of(1L, 2L),
                                1L,
                                1L
                        )
                )));

        // when & then
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andDo(document("post/get-all"));

        verify(postQueryService)
                .findAll(eq(null), eq(null), any());
    }

    @Test
    @DisplayName("categoryId와 seriesId가 모두 있으면 400을 반환한다.")
    void getAllPostsWithCategoryIdAndSeriesId() throws Exception {
        // given
        given(postQueryService.findAll(eq(1L), eq(2L), any()))
                .willThrow(new PostException(PostErrorCode.INVALID_POST_FILTER));

        // when & then
        mockMvc.perform(get("/api/posts")
                        .param("categoryId", "1")
                        .param("seriesId", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("categoryId와 seriesId는 동시에 요청할 수 없습니다."));

        verify(postQueryService)
                .findAll(eq(1L), eq(2L), any());
    }

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
                        .with(apiKey())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(document("post/publish"));

        verify(postCommandService).publishPost(
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
                        .with(apiKey())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(document("post/draft"));

        verify(postCommandService).draftPost(
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
                        .with(apiKey())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                .andDo(document("post/update"));

        verify(postCommandService).updatePost(
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
        mockMvc.perform(delete("/api/posts/1")
                        .with(apiKey()))
                .andExpect(status().isNoContent())
                .andDo(document("post/delete"));

        verify(postCommandService)
                .deletePost(1L);
    }
}