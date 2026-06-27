package com.minsun0714.techblogadminbe.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public record AppSecurityProperties(
        User user,
        Jwt jwt
) {

    public record User(
            String username,
            String password
    ) {
    }

    public record Jwt(
            String secret,
            long tokenTtlSeconds
    ) {
    }
}
