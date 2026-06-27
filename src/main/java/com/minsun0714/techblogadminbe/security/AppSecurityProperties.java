package com.minsun0714.techblogadminbe.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.security")
public record AppSecurityProperties(
        User user,
        Jwt jwt
) {

    public record User(
            @NotBlank
            String username,
            @NotBlank
            String password
    ) {
    }

    public record Jwt(
            @NotBlank
            String secret,
            @NotBlank
            String issuer,
            @Positive
            long tokenTtlSeconds
    ) {
    }
}
