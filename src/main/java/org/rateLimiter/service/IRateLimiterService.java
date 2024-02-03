package org.rateLimiter.service;

public interface IRateLimiterService {
    String limited(String ipAddress) throws Exception;

    String unlimited();
}
