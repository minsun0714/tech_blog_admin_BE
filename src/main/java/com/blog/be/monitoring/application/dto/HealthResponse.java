package com.blog.be.monitoring.application.dto;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.health.SystemHealth;

import java.util.Map;

public record HealthResponse(
        String status,
        String db,
        DiskHealthResponse diskSpace,
        String ping,
        String ssl
) {

    public static HealthResponse from(SystemHealth health) {
        Map<String, HealthComponent> components = health.getComponents();

        return new HealthResponse(
                health.getStatus().toString(),
                getStatus(components, "db").toString(),
                getDiskHealth(components),
                getStatus(components, "ping").toString(),
                getStatus(components, "ssl").toString()
        );
    }

    private static Status getStatus(Map<String, HealthComponent> components, String name) {
        HealthComponent component = components.get(name);

        if (component instanceof Health health) {
            return health.getStatus();
        }

        return Status.UNKNOWN;
    }

    private static DiskHealthResponse getDiskHealth(Map<String, HealthComponent> components) {
        HealthComponent component = components.get("diskSpace");

        if (component instanceof Health health) {
            return DiskHealthResponse.from(health);
        }

        return new DiskHealthResponse(
                Status.UNKNOWN.toString(),
                0L,
                0L,
                0L,
                "",
                false
        );
    }

    public record DiskHealthResponse(
            String status,
            double totalGb,
            double freeGb,
            double usedPercent,
            String path,
            boolean exists
    ) {

        private static final double BYTE_TO_GB = 1024.0 * 1024.0 * 1024.0;

        public static DiskHealthResponse from(Health health) {
            Map<String, Object> details = health.getDetails();

            long total = getLong(details, "total");
            long free = getLong(details, "free");

            return new DiskHealthResponse(
                    health.getStatus().toString(),
                    round(total / BYTE_TO_GB),
                    round(free / BYTE_TO_GB),
                    total == 0 ? 0.0 : round((double) (total - free) / total * 100),
                    (String) details.getOrDefault("path", ""),
                    (Boolean) details.getOrDefault("exists", false)
            );
        }

        private static long getLong(Map<String, Object> details, String key) {
            Object value = details.get(key);
            return value instanceof Number number ? number.longValue() : 0L;
        }

        private static double round(double value) {
            return Math.round(value * 10) / 10.0;
        }
    }
}