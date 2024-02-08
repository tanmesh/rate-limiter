package org.rateLimiter.policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FixedCounterWindowAlgorithm implements RateLimiter {
    private final int maxBucketSize;

    private long windowStart;

    private final int windowSize;

    public Map<String, Bucket> bucketMap;

    public FixedCounterWindowAlgorithm(int bucketSize, int windowSize) {
        this.maxBucketSize = bucketSize;
        this.bucketMap = new ConcurrentHashMap<>();
        this.windowStart = System.nanoTime();
        this.windowSize = windowSize;
    }

    @Override
    public boolean allowRequest(String ipAddress) {
        Bucket bucket = bucketMap.get(ipAddress);
        if (bucket == null) {
            bucket = new Bucket(maxBucketSize);
        }
        System.out.println("Current Bucket size:" + bucket.getCurrentBucketSize());

        refill(bucket);

        double currentBucketSize = bucket.getCurrentBucketSize();
        if (currentBucketSize > 0) {
            bucket.setCurrentBucketSize(currentBucketSize - 1);
            bucketMap.put(ipAddress, bucket);
            return true;
        }

        bucketMap.put(ipAddress, bucket);
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


