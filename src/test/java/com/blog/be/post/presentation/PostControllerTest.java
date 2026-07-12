package com.blog.be.post.presentation;

import com.blog.be.post.application.PostCommandService;
import com.blog.be.post.application.PostQueryService;
import com.blog.be.post.application.dto.PostCountResponse;
import com.blog.be.post.domain.PostErrorCode;
import com.blog.be.post.domain.PostException;
import com.blog.be.post.domain.Post;
import com.blog.be.post.domain.PublishStatus;
import com.blog.be.post.presentation.dto.PostPublishRequest;
import com.blog.be.post.presentation.dto.PostResponse;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.blog.be.support.MockMvcUtils.apiKey;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @DisplayName("저장된 게시물의 개수와 임시저장된 게시물의 개수를 조회한다.")
    void getPostsCount() throws Exception {
        // given
        long publishedPostCount = 3;
        long draftedPostCount = 5;
        given(postQueryService.getPostsCountByPublishStatus())
                .willReturn(PostCountResponse.of(publishedPostCount, draftedPostCount));

        // when & then
        mockMvc.perform(get("/api/posts/count"))
                .andExpect(status().isOk())
                .andDo(document("post/get-count"));
    }

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
                        1L,
                        PublishStatus.PUBLISHED
                ));

        // when & then
        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andDo(document("post/get-detail"));
    }

    @Test
    @DisplayName("카테고리 필터로 저장된 게시글을 조회한다.")
    void getAllPublishedPostsByCategoryId() throws Exception {
        // given
        given(postQueryService.getPagedPosts(eq(PublishStatus.PUBLISHED), eq(1L), eq(null), eq(null), any()))
                .willReturn(new PageImpl<>(List.of()));

        // when & then
        mockMvc.perform(get("/api/posts")
                        .param("categoryId", "1"))
                .andExpect(status().isOk());

        verify(postQueryService)
                .getPagedPosts(eq(PublishStatus.PUBLISHED), eq(1L), eq(null), eq(null), any());
    }

    @Test
    @DisplayName("카테고리 필터로 임시저장된 게시글을 조회한다.")
    void getAllDraftedPostsByCategoryId() throws Exception {
        // given
        given(postQueryService.getPagedPosts(eq(PublishStatus.DRAFTED), eq(1L), eq(null), eq(null), any()))
                .willReturn(new PageImpl<>(List.of()));

        // when & then
        mockMvc.perform(get("/api/posts")
                        .param("publishStatus", "DRAFTED")
                        .param("categoryId", "1"))
                .andExpect(status().isOk());

        verify(postQueryService)
                .getPagedPosts(eq(PublishStatus.DRAFTED), eq(1L), eq(null), eq(null), any());
    }

    @Test
    @DisplayName("시리즈 필터로 게시글을 조회한다.")
    void getAllPostsBySeriesId() throws Exception {
        // given
        given(postQueryService.getPagedPosts(eq(PublishStatus.PUBLISHED), eq(null), eq(2L), eq(null),any()))
                .willReturn(new PageImpl<>(List.of()));

        // when & then
        mockMvc.perform(get("/api/posts")
                        .param("seriesId", "2"))
                .andExpect(status().isOk());

        verify(postQueryService)
                .getPagedPosts(eq(PublishStatus.PUBLISHED), eq(null), eq(2L), eq(null), any());
    }

    @Test
    @DisplayName("태그 필터로 게시글을 조회한다.")
    void getAllPostsByTagId() throws Exception {
        // given
        given(postQueryService.getPagedPosts(eq(PublishStatus.PUBLISHED), eq(null), eq(null), eq(2L), any()))
                .willReturn(new PageImpl<>(List.of()));

        // when & then
        mockMvc.perform(get("/api/posts")
                        .param("tagId", "2"))
                .andExpect(status().isOk());

        verify(postQueryService)
                .getPagedPosts(eq(PublishStatus.PUBLISHED), eq(null), eq(null), eq(2L), any());
    }

    @Test
    @DisplayName("필터가 없으면 전체 게시글을 페이지네이션 조회한다.")
    void getAllPosts() throws Exception {
        // given
        Post post = Post.publish(
                "제목",
                "내용",
                Set.of(1L, 2L),
                1L,
                1L,
                PublishStatus.PUBLISHED
        );

        List<String> tagNames = List.of("java", "spring");

        PostResponse postResponse = PostResponse.of(post, tagNames);
        given(postQueryService.getPagedPosts(eq(PublishStatus.PUBLISHED), eq(null), eq(null), eq(null), any()))
                .willReturn(new PageImpl<>(List.of(postResponse)));

        // when & then
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andDo(document("post/get-all"));

        verify(postQueryService)
                .getPagedPosts(eq(PublishStatus.PUBLISHED), eq(null), eq(null), eq(null), any());
    }

    @Test
    @DisplayName("categoryId와 seriesId가 모두 있으면 400을 반환한다.")
    void getAllPostsWithCategoryIdAndSeriesId() throws Exception {
        // given
        given(postQueryService.getPagedPosts(eq(PublishStatus.PUBLISHED), eq(1L), eq(2L), eq(null), any()))
                .willThrow(new PostException(PostErrorCode.INVALID_POST_FILTER));

        // when & then
        mockMvc.perform(get("/api/posts")
                        .param("categoryId", "1")
                        .param("seriesId", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("categoryId와 seriesId는 동시에 요청할 수 없습니다."));

        verify(postQueryService)
                .getPagedPosts(eq(PublishStatus.PUBLISHED), eq(1L), eq(2L), eq(null), any());
    }

    @Test
    @DisplayName("게시글을 발행한다.")
    void publishPost() throws Exception {
        // given
        String uuid = UUID.randomUUID().toString();
        PostPublishRequest request = new PostPublishRequest(
                "제목",
                "내용",
                Set.of("Spring", "JPA"),
                1L,
                1L,
                uuid,
                PublishStatus.PUBLISHED
        );

        // when & then
        mockMvc.perform(post("/api/posts")
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
                eq(1L),
                eq(uuid),
                eq(PublishStatus.PUBLISHED)
        );
    }

    @Test
    @DisplayName("게시글을 임시 저장한다.")
    void draftPost() throws Exception {
        // given
        String uuid = UUID.randomUUID().toString();
        PostPublishRequest request = new PostPublishRequest(
                "제목",
                "내용",
                Set.of("Spring"),
                1L,
                1L,
                uuid,
                PublishStatus.DRAFTED
        );

        // when & then
        mockMvc.perform(post("/api/posts")
                        .with(apiKey())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(document("post/draft"));

        verify(postCommandService).publishPost(
                eq("제목"),
                eq("내용"),
                eq(Set.of("Spring")),
                eq(1L),
                eq(1L),
                eq(uuid),
                eq(PublishStatus.DRAFTED)
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
                3L,
                PublishStatus.PUBLISHED
        );

        // when & then
        mockMvc.perform(put("/api/posts/1")
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
                eq(3L),
                eq(PublishStatus.PUBLISHED)
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