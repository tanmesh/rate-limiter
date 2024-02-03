package org.rateLimiter.policy;

public interface RateLimiter {
    boolean allowRequest(String ipAddress);
}
