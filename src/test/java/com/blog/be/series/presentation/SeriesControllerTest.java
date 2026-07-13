package com.blog.be.series.presentation;

import com.blog.be.series.application.SeriesCommandService;
import com.blog.be.series.application.SeriesQueryService;
import com.blog.be.series.infrastructure.repository.projection.SeriesResponseDto;
import com.blog.be.series.presentation.dto.SeriesCreateRequest;
import com.blog.be.series.presentation.dto.SeriesUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.blog.be.support.MockMvcUtils.apiKey;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(SeriesController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class SeriesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SeriesQueryService seriesQueryService;

    @MockitoBean
    private SeriesCommandService seriesCommandService;

    @Test
    @DisplayName("시리즈를 모두 조회한다.")
    void findAllSeries() throws Exception {
        // given
        SeriesResponseDto series1 = new SeriesResponseDto(
                1L,
                "Spring",
                3L
        );

        SeriesResponseDto series2 = new SeriesResponseDto(
                2L,
                "Java",
                5L
        );

        List<SeriesResponseDto> content = List.of(series1, series2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<SeriesResponseDto> mockPage = new PageImpl<>(content, pageable, content.size());

        given(seriesQueryService.findAll(pageable))
                .willReturn(mockPage);

        // when & then
        mockMvc.perform(get("/api/series")
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Spring"))     // content[0]으로 수정
                .andExpect(jsonPath("$.content[0].postCount").value(3)) // content[0]으로 수정
                .andDo(document("series/get-all"));

        verify(seriesQueryService)
                .findAll(pageable);
    }

    @Test
    @DisplayName("시리즈를 생성한다.")
    void createSeries() throws Exception {
        // given
        SeriesCreateRequest request = new SeriesCreateRequest("Spring");

        // when & then
        mockMvc.perform(post("/api/series")
                        .with(apiKey())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("series/create"));

        verify(seriesCommandService)
                .createSeries("Spring");
    }

    @Test
    @DisplayName("시리즈 이름을 변경한다.")
    void updateSeriesName() throws Exception {
        // given
        SeriesUpdateRequest request = new SeriesUpdateRequest("DDD");

        // when & then
        mockMvc.perform(patch("/api/series/1")
                        .with(apiKey())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                .andDo(document("series/update"));

        verify(seriesCommandService)
                .updateSeriesName(1L, "DDD");
    }

    @Test
    @DisplayName("시리즈를 삭제한다.")
    void deleteSeries() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/series/1")
                        .with(apiKey()))
                .andExpect(status().isNoContent())
                .andDo(document("series/delete"));

        verify(seriesCommandService)
                .deleteSeries(1L);
    }
}