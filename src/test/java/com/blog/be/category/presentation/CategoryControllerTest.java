package com.blog.be.category.presentation;

import com.blog.be.category.application.CategoryCommandService;
import com.blog.be.category.application.CategoryQueryService;
import com.blog.be.category.presentation.dto.CategoryChangeParentRequest;
import com.blog.be.category.presentation.dto.CategoryCreateRequest;
import com.blog.be.category.presentation.dto.CategoryUpdateNameRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CategoryQueryService categoryQueryService;

    @MockitoBean
    CategoryCommandService categoryCommandService;

    @Test
    void getAllCategories() throws Exception {
        // given
        given(categoryQueryService.getAllCategories())
                .willReturn(List.of());

        // when & then
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());
    }


    @Test
    void createCategory() throws Exception {

        CategoryCreateRequest request =
                new CategoryCreateRequest("Backend", null);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(categoryCommandService)
                .createCategory("Backend", null);
    }

    @Test
    void updateCategoryName() throws Exception {

        CategoryUpdateNameRequest request =
                new CategoryUpdateNameRequest("Spring");

        mockMvc.perform(patch("/api/categories/1/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(categoryCommandService)
                .updateCategoryName(1L, "Spring");
    }

    @Test
    void changeParent() throws Exception {

        CategoryChangeParentRequest request =
                new CategoryChangeParentRequest(10L);

        mockMvc.perform(patch("/api/categories/1/parent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(categoryCommandService)
                .changeParent(1L, 10L);
    }

    @Test
    void deleteCategory() throws Exception {

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryCommandService)
                .deleteCategory(1L);
    }
}