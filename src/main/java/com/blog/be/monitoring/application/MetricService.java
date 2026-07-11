package com.blog.be.monitoring.application;

import com.blog.be.monitoring.application.dto.MeterResponse;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MetricService {

    private static final double BYTE_TO_MB = 1024.0 * 1024.0;

    private final MeterRegistry meterRegistry;

    public MeterResponse getMetrics() {
        return MeterResponse.of(
                systemCpuUsage(),
                processCpuUsage(),
                heapUsed(),
                heapMax(),
                nonHeapUsed(),
                liveThreads(),
                daemonThreads(),
                peakThreads(),
                gcCount(),
                gcPauseTimeMillis(),
                successRequestCount(),
                processUptime()
        );
    }

    private double systemCpuUsage() {
        return toPercent(gaugeValue("system.cpu.usage"));
    }

    private double processCpuUsage() {
        return toPercent(gaugeValue("process.cpu.usage"));
    }

    private double heapUsed() {
        return toMb(gaugeValue("jvm.memory.used", "area", "heap"));
    }

    private double heapMax() {
        return toMb(gaugeValue("jvm.memory.max", "area", "heap"));
    }

    private double nonHeapUsed() {
        return toMb(gaugeValue("jvm.memory.used", "area", "nonheap"));
    }

    private int liveThreads() {
        return (int) gaugeValue("jvm.threads.live");
    }

    private int daemonThreads() {
        return (int) gaugeValue("jvm.threads.daemon");
    }

    private int peakThreads() {
        return (int) gaugeValue("jvm.threads.peak");
    }

    private long gcCount() {
        Timer timer = timer("jvm.gc.pause");
        return timer != null ? timer.count() : 0L;
    }

    private double gcPauseTimeMillis() {
        Timer timer = timer("jvm.gc.pause");
        return timer != null ? round(timer.totalTime(TimeUnit.MILLISECONDS)) : 0.0;
    }

    private long successRequestCount() {
        Timer timer = timer("http.server.requests", "status", "200");
        return timer != null ? timer.count() : 0L;
    }

    private double processUptime() {
        return round(gaugeValue("process.uptime"));
    }

    private double gaugeValue(String name) {
        Gauge gauge = meterRegistry.find(name).gauge();
        return gauge != null ? gauge.value() : 0.0;
    }

    private double gaugeValue(String name, String tag, String value) {
        Gauge gauge = meterRegistry.find(name)
                .tag(tag, value)
                .gauge();
        return gauge != null ? gauge.value() : 0.0;
    }

    private Timer timer(String name) {
        return meterRegistry.find(name).timer();
    }

    private Timer timer(String name, String tag, String value) {
        return meterRegistry.find(name)
                .tag(tag, value)
                .timer();
    }

    private double toPercent(double value) {
        return round(value * 100);
    }

    private double toMb(double bytes) {
        return round(bytes / BYTE_TO_MB);
    }

    private double round(double value) {
        return Math.round(value * 10) / 10.0;
    }
}