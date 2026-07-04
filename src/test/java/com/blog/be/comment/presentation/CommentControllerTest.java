package com.blog.be.comment.presentation;

import com.blog.be.comment.application.CommentCommandService;
import com.blog.be.comment.presentation.dto.CommentCreateRequest;
import com.blog.be.comment.presentation.dto.CommentUpdateRequest;
import com.blog.be.comment.presentation.dto.ReplyCreateRequest;
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

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentCommandService commentCommandService;

    @Test
    @DisplayName("루트 댓글을 작성한다.")
    void createRootComment() throws Exception {
        // given
        CommentCreateRequest request = new CommentCreateRequest("댓글");

        // when & then
        mockMvc.perform(post("/api/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(commentCommandService)
                .createRootComment(1L, "댓글");
    }

    @Test
    @DisplayName("대댓글을 작성한다.")
    void createReply() throws Exception {
        // given
        ReplyCreateRequest request = new ReplyCreateRequest(1L, "대댓글");

        // when & then
        mockMvc.perform(post("/api/comments/10/replies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(commentCommandService)
                .createReply(10L, 1L, "대댓글");
    }

    @Test
    @DisplayName("댓글을 수정한다.")
    void updateComment() throws Exception {
        // given
        CommentUpdateRequest request = new CommentUpdateRequest("수정된 댓글");

        // when & then
        mockMvc.perform(patch("/api/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(commentCommandService)
                .updateComment(1L, "수정된 댓글");
    }

    @Test
    @DisplayName("댓글을 삭제한다.")
    void deleteComment() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/comments/1"))
                .andExpect(status().isNoContent());

        verify(commentCommandService)
                .deleteComment(1L);
    }
}