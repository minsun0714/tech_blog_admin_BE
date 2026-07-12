package com.blog.be.series.presentation;

import com.blog.be.series.application.SeriesCommandService;
import com.blog.be.series.application.SeriesQueryService;
import com.blog.be.series.infrastructure.persistence.SeriesJpaEntity;
import com.blog.be.series.presentation.dto.SeriesCreateRequest;
import com.blog.be.series.presentation.dto.SeriesCreateResponse;
import com.blog.be.series.presentation.dto.SeriesListResponse;
import com.blog.be.series.presentation.dto.SeriesUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/series")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesQueryService seriesQueryService;

    private final SeriesCommandService seriesCommandService;

    @GetMapping
    public ResponseEntity<SeriesListResponse> getAllSeries() {
        List<SeriesJpaEntity> seriesList = seriesQueryService.findAll();
        return ResponseEntity.ok(SeriesListResponse.from(seriesList));
    }

    @PostMapping
    public ResponseEntity<SeriesCreateResponse> createSeries(
            @RequestBody SeriesCreateRequest request
    ) {
        Long newSeriesId = seriesCommandService.createSeries(request.name());

        return ResponseEntity.ok().body(SeriesCreateResponse.of(newSeriesId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateSeriesName(
            @PathVariable Long id,
            @RequestBody SeriesUpdateRequest request
    ) {
        seriesCommandService.updateSeriesName(id, request.name());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeries(
            @PathVariable Long id
    ) {
        seriesCommandService.deleteSeries(id);

        return ResponseEntity.noContent().build();
    }
}