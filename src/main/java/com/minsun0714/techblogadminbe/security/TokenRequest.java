package com.minsun0714.techblogadminbe.security;

public record TokenRequest(
        String username,
        String password
) {
}
