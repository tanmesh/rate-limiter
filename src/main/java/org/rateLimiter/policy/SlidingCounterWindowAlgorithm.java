package org.rateLimiter.policy;

import org.rateLimiter.service.RedisService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SlidingCounterWindowAlgorithm implements RateLimiter {
    private final int maxBucketSize;
    private long windowStart;
    private final int windowSize;
    private RedisService redisService;

    public SlidingCounterWindowAlgorithm(int bucketSize, int windowSize) {
        this.maxBucketSize = bucketSize;
        this.windowStart = System.nanoTime();
        this.windowSize = windowSize;
        this.redisService = new RedisService();
    }

    @Override
    public boolean allowRequest(String ipAddress) {
        Bucket bucket = redisService.get(ipAddress);
        if (bucket == null) {
            bucket = new Bucket(maxBucketSize);
        }
        System.out.println("Current Bucket size:" + bucket.getTimestamps().size());

        refill(bucket);

        List<Long> timestamps = bucket.getTimestamps();
        double currentBucketSize = timestamps.size();
        if (currentBucketSize < maxBucketSize) {
            timestamps.add(System.nanoTime());
            bucket.setTimestamps(timestamps);
            redisService.add(ipAddress, bucket);
            return true;
        }

        redisService.add(ipAddress, bucket);
        return false;
    }

    private void refill(Bucket bucket) {
        long maxAllowed = System.nanoTime() - windowSize * 1_000_000_000L;

        List<Long> timestamps = bucket.getTimestamps();
        timestamps.removeIf(timestamp -> timestamp < maxAllowed);
        bucket.setTimestamps(timestamps);
    }
}
