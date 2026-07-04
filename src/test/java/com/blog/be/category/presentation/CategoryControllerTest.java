package com.blog.be.category.presentation;

import com.blog.be.category.application.CategoryCommandService;
import com.blog.be.category.application.CategoryQueryService;
import com.blog.be.category.application.dto.CategoryNode;
import com.blog.be.category.infrastructure.persistence.CategoryJpaEntity;
import com.blog.be.category.presentation.dto.CategoryChangeParentRequest;
import com.blog.be.category.presentation.dto.CategoryCreateRequest;
import com.blog.be.category.presentation.dto.CategoryUpdateNameRequest;
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
@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryQueryService categoryQueryService;

    @MockitoBean
    private CategoryCommandService categoryCommandService;

    @Test
    @DisplayName("전체 카테고리를 계층 구조로 조회한다")
    void getAllCategories() throws Exception {
        // given
        CategoryNode backend = CategoryNode.from(
                CategoryJpaEntity.builder()
                        .id(1L)
                        .name("Backend")
                        .parentId(null)
                        .build()
        );

        CategoryNode spring = CategoryNode.from(
                CategoryJpaEntity.builder()
                        .id(2L)
                        .name("Spring")
                        .parentId(1L)
                        .build()
        );

        backend.getChildren().add(spring);

        given(categoryQueryService.getAllCategories())
                .willReturn(List.of(backend));

        // when & then
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryList[0].categoryId").value(1))
                .andExpect(jsonPath("$.categoryList[0].categoryName").value("Backend"))
                .andExpect(jsonPath("$.categoryList[0].childrenCategoryList[0].categoryId").value(2))
                .andExpect(jsonPath("$.categoryList[0].childrenCategoryList[0].categoryName").value("Spring"))
                .andDo(document("category/get-all"));
    }

    @Test
    @DisplayName("카테고리를 생성한다")
    void createCategory() throws Exception {
        // given
        CategoryCreateRequest request =
                new CategoryCreateRequest("Backend", null);

        // when & then
        mockMvc.perform(post("/api/categories")
                        .with(apiKey())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(document("category/create"));

        verify(categoryCommandService)
                .createCategory("Backend", null);
    }

    @Test
    @DisplayName("카테고리 이름을 수정한다")
    void updateCategoryName() throws Exception {
        // given
        CategoryUpdateNameRequest request =
                new CategoryUpdateNameRequest("Spring");

        // when & then
        mockMvc.perform(patch("/api/categories/1/name")
                        .with(apiKey())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                .andDo(document("category/update-name"));

        verify(categoryCommandService)
                .updateCategoryName(1L, "Spring");
    }

    @Test
    @DisplayName("카테고리의 부모를 변경한다")
    void changeParent() throws Exception {
        // given
        CategoryChangeParentRequest request =
                new CategoryChangeParentRequest(10L);

        // when & then
        mockMvc.perform(patch("/api/categories/1/parent")
                        .with(apiKey())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                .andDo(document("category/change-parent"));

        verify(categoryCommandService)
                .changeParent(1L, 10L);
    }

    @Test
    @DisplayName("카테고리를 삭제한다")
    void deleteCategory() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/categories/1")
                        .with(apiKey()))
                .andExpect(status().isNoContent())
                .andDo(document("category/delete"));

        verify(categoryCommandService)
                .deleteCategory(1L);
    }
}