package org.rateLimiter.policy;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.min;

public class TokenBucketAlgorithm implements RateLimiter {
    private final int refillRate;
    private final double maxBucketSize;
    public Map<String, Bucket> bucketMap;

    public TokenBucketAlgorithm(int refillRate, int bucketSize) {
        this.refillRate = refillRate;
        this.maxBucketSize = bucketSize;

        this.bucketMap = new HashMap<>();
    }

    @Override
    synchronized public boolean allowRequest(String ipAddress) {
        Bucket bucket = bucketMap.get(ipAddress);
        if(bucket == null) {
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
        long now = System.nanoTime();
        double tokenToAdd = (now - bucket.getLastRefillTimestamp()) * refillRate / 1e9;

        double currentBucketSize = bucket.getCurrentBucketSize();
        currentBucketSize = min(currentBucketSize + tokenToAdd, maxBucketSize);
        bucket.setCurrentBucketSize(currentBucketSize);
        bucket.setLastRefillTimestamp(now);
    }
}
