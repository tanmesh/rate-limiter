package org.rateLimiter.policy;

import org.rateLimiter.service.RedisService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FixedCounterWindowAlgorithm implements RateLimiter {
    private final int maxBucketSize;
    private long windowStart;
    private final int windowSize;
    private RedisService redisService;

    public FixedCounterWindowAlgorithm(int bucketSize, int windowSize) {
        this.maxBucketSize = bucketSize;
        this.windowStart = System.nanoTime();
        this.windowSize = windowSize;
        this.redisService = new RedisService();
    }

    @Override
    synchronized public boolean allowRequest(String ipAddress) {
        Bucket bucket = redisService.get(ipAddress);
        if (bucket == null) {
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
        long nextAllowed = windowStart + windowSize * 1_000_000_000L;

        if(System.nanoTime() > nextAllowed) {
            windowStart = System.nanoTime();
            bucket.setCurrentBucketSize(maxBucketSize);
        }
    }
}


