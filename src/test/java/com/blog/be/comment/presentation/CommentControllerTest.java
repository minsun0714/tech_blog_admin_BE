package com.blog.be.comment.presentation;

import com.blog.be.comment.application.CommentCommandService;
import com.blog.be.comment.application.CommentQueryService;
import com.blog.be.comment.application.dto.CommentNode;
import com.blog.be.comment.infrastructure.persistence.CommentJpaEntity;
import com.blog.be.comment.presentation.dto.CommentCreateRequest;
import com.blog.be.comment.presentation.dto.CommentDeleteRequest;
import com.blog.be.comment.presentation.dto.CommentUpdateRequest;
import com.blog.be.comment.presentation.dto.ReplyCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.blog.be.support.MockMvcUtils.apiKey;
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
@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentQueryService commentQueryService;

    @MockitoBean
    private CommentCommandService commentCommandService;

    @Test
    @DisplayName("게시글의 댓글 목록을 계층 구조로 조회한다.")
    void getCommentList() throws Exception {
        // given
        CommentNode root = CommentNode.from(
                CommentJpaEntity.builder()
                        .id(1L)
                        .content("루트 댓글")
                        .parentCommentId(null)
                        .postId(1L)
                        .build()
        );

        CommentNode reply = CommentNode.from(
                CommentJpaEntity.builder()
                        .id(2L)
                        .content("대댓글")
                        .parentCommentId(1L)
                        .postId(1L)
                        .build()
        );

        root.children().add(reply);

        given(commentQueryService.getAllCommentsByPostId(1L))
                .willReturn(List.of(root));

        // when & then
        mockMvc.perform(get("/api/posts/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentResponseList[0].commentId").value(1))
                .andExpect(jsonPath("$.commentResponseList[0].content").value("루트 댓글"))
                .andExpect(jsonPath("$.commentResponseList[0].childrenCommentList[0].commentId").value(2))
                .andExpect(jsonPath("$.commentResponseList[0].childrenCommentList[0].content").value("대댓글"))
                .andDo(document("comment/get-all"));
    }

    @Test
    @DisplayName("루트 댓글을 작성한다.")
    void createRootComment() throws Exception {
        // given
        CommentCreateRequest request = new CommentCreateRequest("user1", "1234", "댓글");

        // when & then
        mockMvc.perform(post("/api/posts/1/comments")
                        .with(apiKey())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(document("comment/create-root"));

        verify(commentCommandService)
                .createRootComment(1L, "user1", "1234", "댓글");
    }

    @Test
    @DisplayName("대댓글을 작성한다.")
    void createReply() throws Exception {
        // given
        ReplyCreateRequest request = new ReplyCreateRequest(1L, "user1", "1234",  "대댓글");

        // when & then
        mockMvc.perform(post("/api/comments/10/replies")
                        .with(apiKey())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(document("comment/create-reply"));

        verify(commentCommandService)
                .createReply(10L, 1L, "user1", "1234", "대댓글");
    }

    @Test
    @DisplayName("댓글을 수정한다.")
    void updateComment() throws Exception {
        // given
        CommentUpdateRequest request = new CommentUpdateRequest("user1", "1234", "수정된 댓글");

        // when & then
        mockMvc.perform(patch("/api/comments/1")
                        .with(apiKey())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                .andDo(document("comment/update"));

        verify(commentCommandService)
                .updateComment(1L, "1234", "수정된 댓글");
    }

    @Test
    @DisplayName("댓글을 삭제한다.")
    void deleteComment() throws Exception {
        // when & then
        CommentDeleteRequest request = new CommentDeleteRequest("1234");

        mockMvc.perform(delete("/api/comments/1")
                        .with(apiKey())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                .andDo(document("comment/delete"));

        verify(commentCommandService)
                .deleteComment(1L, "1234");
    }
}