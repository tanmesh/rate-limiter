package org.rateLimiter.policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SlidingCounterWindowAlgorithm implements RateLimiter {
    private final int maxBucketSize;

    private long windowStart;
    private final int windowSize;

    public ConcurrentHashMap<String, Bucket> bucketMap;

    public SlidingCounterWindowAlgorithm(int bucketSize, int windowSize) {
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
        System.out.println("Current Bucket size:" + bucket.getTimestamps().size());

        refill(bucket);

        List<Long> timestamps = bucket.getTimestamps();
        double currentBucketSize = timestamps.size();
        if (currentBucketSize < maxBucketSize) {
            timestamps.add(System.nanoTime());
            bucket.setTimestamps(timestamps);
            bucketMap.put(ipAddress, bucket);
            return true;
        }

        bucketMap.put(ipAddress, bucket);
        return false;
    }

    private void refill(Bucket bucket) {
        long maxAllowed = System.nanoTime() - windowSize * 1_000_000_000L;

        List<Long> timestamps = bucket.getTimestamps();
        timestamps.removeIf(timestamp -> timestamp < maxAllowed);
        bucket.setTimestamps(timestamps);
    }
}
