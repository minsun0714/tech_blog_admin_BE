package com.minsun0714.techblogadminbe.security;

public record TokenResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}
