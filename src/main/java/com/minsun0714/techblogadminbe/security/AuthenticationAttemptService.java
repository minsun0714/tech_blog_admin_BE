package com.minsun0714.techblogadminbe.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final Duration WINDOW = Duration.ofMinutes(5);

    private final Map<String, Attempt> attempts = new ConcurrentHashMap<>();

    public boolean isBlocked(String username) {
        Attempt attempt = attempts.get(username);
        if (attempt == null) {
            return false;
        }

        if (attempt.isExpired()) {
            attempts.remove(username);
            return false;
        }

        return attempt.count() >= MAX_ATTEMPTS;
    }

    public void onFailure(String username) {
        attempts.compute(username, (key, existing) -> {
            if (existing == null || existing.isExpired()) {
                return new Attempt(1, Instant.now());
            }
            return new Attempt(existing.count() + 1, existing.firstFailedAt());
        });
    }

    public void onSuccess(String username) {
        attempts.remove(username);
    }

    private record Attempt(int count, Instant firstFailedAt) {
        private boolean isExpired() {
            return firstFailedAt.plus(WINDOW).isBefore(Instant.now());
        }
    }
}
