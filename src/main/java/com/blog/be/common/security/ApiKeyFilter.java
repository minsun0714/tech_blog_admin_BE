package com.blog.be.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";

    private final String apiKey;

    public ApiKeyFilter(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String requestKey = request.getHeader(API_KEY_HEADER);

        if (!isValidApiKey(requestKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                "api-key",
                null,
                AuthorityUtils.createAuthorityList("ROLE_ADMIN")
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private boolean isValidApiKey(String requestKey) {
        byte[] expected = apiKey.getBytes(StandardCharsets.UTF_8);
        byte[] provided = requestKey == null
                ? new byte[0]
                : requestKey.getBytes(StandardCharsets.UTF_8);

        return MessageDigest.isEqual(expected, provided);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "GET".equalsIgnoreCase(request.getMethod());
    }
}
