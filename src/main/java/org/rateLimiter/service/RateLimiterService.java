package org.rateLimiter.service;

import org.rateLimiter.policy.RateLimiter;

public class RateLimiterService implements IRateLimiterService {
    private RateLimiter rateLimiter;
    private IRedisService redisService;

    public RateLimiterService(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
        this.redisService = new RedisService();
    }

    @Override
    public String limited(String ipAddress) throws Exception {
        if (rateLimiter.allowRequest(ipAddress)) {
            return "Limited, don't over use me!";
        } else {
            throw new Exception("Request is dropped");
        }
    }

    @Override
    public String unlimited() {
        return "Unlimited! Let's Go!";
    }
}
