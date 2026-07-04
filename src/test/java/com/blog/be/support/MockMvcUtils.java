package com.blog.be.support;

import org.springframework.test.web.servlet.request.RequestPostProcessor;

public final class MockMvcUtils {

    private MockMvcUtils() {
    }

    public static RequestPostProcessor apiKey() {
        return request -> {
            request.addHeader("X-API-KEY", "test-api-key");
            return request;
        };
    }
}
