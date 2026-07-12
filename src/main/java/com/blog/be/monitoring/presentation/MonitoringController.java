package com.blog.be.monitoring.presentation;

import com.blog.be.monitoring.application.HealthService;
import com.blog.be.monitoring.application.MetricService;
import com.blog.be.monitoring.application.dto.HealthResponse;
import com.blog.be.monitoring.application.dto.MeterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/monitoring")
@RequiredArgsConstructor
public class MonitoringController {

    private final HealthService healthService;
    private final MetricService metricService;

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> getHealthInfo() {
        HealthResponse health = healthService.getHealth();

        return ResponseEntity.ok().body(health);
    }

    @GetMapping("/metrics")
    public ResponseEntity<MeterResponse> getMetricsInfo() {
        MeterResponse metrics = metricService.getMetrics();

        return ResponseEntity.ok().body(metrics);
    }
}
