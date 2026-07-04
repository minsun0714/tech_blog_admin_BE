package com.blog.be.tag.presentation;

import com.blog.be.tag.application.TagCommandService;
import com.blog.be.tag.presentation.dto.TagCreateRequest;
import com.blog.be.tag.presentation.dto.TagUpdateRequest;
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

import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(TagController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TagCommandService tagCommandService;

    @Test
    @DisplayName("태그를 생성한다.")
    void createTag() throws Exception {
        // given
        TagCreateRequest request = new TagCreateRequest("Spring");

        // when & then
        mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(document("tag/create"));

        verify(tagCommandService)
                .upsertTag("Spring");
    }

    @Test
    @DisplayName("태그 이름을 변경한다.")
    void updateTagName() throws Exception {
        // given
        TagUpdateRequest request = new TagUpdateRequest("JPA");

        // when & then
        mockMvc.perform(patch("/api/tags/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                .andDo(document("tag/update"));

        verify(tagCommandService)
                .updateTagName(1L, "JPA");
    }

    @Test
    @DisplayName("태그를 삭제한다.")
    void deleteTag() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/tags/1"))
                .andExpect(status().isNoContent())
                .andDo(document("tag/delete"));

        verify(tagCommandService)
                .deleteTag(1L);
    }
}