package com.blog.be.monitoring.application;

import com.blog.be.monitoring.application.dto.HealthResponse;
import com.blog.be.monitoring.application.dto.MeterResponse;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.SystemHealth;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthService {

    private final HealthEndpoint healthEndpoint;

    public HealthResponse getHealth() {
        HealthComponent healthComponent = healthEndpoint.health();

        if (healthComponent instanceof SystemHealth systemHealth) {
            return HealthResponse.from(systemHealth);
        }

        throw new IllegalStateException("HealthComponent가 SystemHealth가 아닙니다.");
    }
}