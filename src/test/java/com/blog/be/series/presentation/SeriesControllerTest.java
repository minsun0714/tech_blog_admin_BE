package com.blog.be.series.presentation;

import com.blog.be.series.application.SeriesCommandService;
import com.blog.be.series.presentation.dto.SeriesCreateRequest;
import com.blog.be.series.presentation.dto.SeriesUpdateRequest;
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
@WebMvcTest(SeriesController.class)
@AutoConfigureMockMvc(addFilters = false)
class SeriesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SeriesCommandService seriesCommandService;

    @Test
    @DisplayName("시리즈를 생성한다.")
    void createSeries() throws Exception {
        // given
        SeriesCreateRequest request = new SeriesCreateRequest("Spring");

        // when & then
        mockMvc.perform(post("/api/series")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(seriesCommandService)
                .updateSeriesName(1L, "DDD");
    }

    @Test
    @DisplayName("시리즈를 삭제한다.")
    void deleteSeries() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/series/1"))
                .andExpect(status().isNoContent());

        verify(seriesCommandService)
                .deleteSeries(1L);
    }
}