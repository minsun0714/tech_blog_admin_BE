package com.blog.be.monitoring.application.dto;

public record MeterResponse(
        double systemCpuUsage,
        double processCpuUsage,
        double heapUsed,
        double heapMax,
        double nonHeapUsed,
        int liveThreads,
        int daemonThreads,
        int peakThreads,
        long gcCount,
        double gcPauseTimeMillis,
        long successRequestCount,
        double processUptime
) {

    public static MeterResponse of(
            double systemCpuUsage,
            double processCpuUsage,
            double heapUsed,
            double heapMax,
            double nonHeapUsed,
            int liveThreads,
            int daemonThreads,
            int peakThreads,
            long gcCount,
            double gcPauseTimeMillis,
            long successRequestCount,
            double processUptime
    ) {
        return new MeterResponse(
                systemCpuUsage,
                processCpuUsage,
                heapUsed,
                heapMax,
                nonHeapUsed,
                liveThreads,
                daemonThreads,
                peakThreads,
                gcCount,
                gcPauseTimeMillis,
                successRequestCount,
                processUptime
        );
    }
}