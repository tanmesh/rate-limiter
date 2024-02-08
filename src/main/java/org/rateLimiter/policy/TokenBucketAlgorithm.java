package org.rateLimiter.policy;

import org.rateLimiter.service.IRateLimiterService;
import org.rateLimiter.service.RateLimiterService;
import org.rateLimiter.service.RedisService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Math.min;

public class TokenBucketAlgorithm implements RateLimiter {
    private final int refillRate;
    private final double maxBucketSize;
    private RedisService redisService;

    public TokenBucketAlgorithm(int refillRate, int bucketSize) {
        this.refillRate = refillRate;
        this.maxBucketSize = bucketSize;
        this.redisService = new RedisService();
    }

    @Override
    synchronized public boolean allowRequest(String ipAddress) {
        Bucket bucket = redisService.get(ipAddress);
        if(bucket == null) {
            bucket = new Bucket(maxBucketSize);
        }
        System.out.println("Current Bucket size:" + bucket.getCurrentBucketSize());

        refill(bucket);

        double currentBucketSize = bucket.getCurrentBucketSize();
        if (currentBucketSize > 0) {
            bucket.setCurrentBucketSize(currentBucketSize - 1);

            redisService.add(ipAddress, bucket);

            return true;
        }

        redisService.add(ipAddress, bucket);
        return false;
    }

    private void refill(Bucket bucket) {
        long now = System.nanoTime();
        long tokenToAdd = (long) ((now - bucket.getLastRefillTimestamp()) * refillRate / 1e9);

        System.out.println("tokenToAdd: " + tokenToAdd);

        double currentBucketSize = bucket.getCurrentBucketSize();
        currentBucketSize = min(currentBucketSize + tokenToAdd, maxBucketSize);
        bucket.setCurrentBucketSize(currentBucketSize);
        bucket.setLastRefillTimestamp(now);
    }
}
