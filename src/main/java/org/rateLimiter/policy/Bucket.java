package org.rateLimiter.policy;

import java.util.ArrayList;
import java.util.List;

public class Bucket {
    private long lastRefillTimestamp;
    private double currentBucketSize;
    private List<Long> timestamps;

    public Bucket() {
    }

    public Bucket(double bucketSize) {
        this.lastRefillTimestamp = System.nanoTime();
        this.currentBucketSize = bucketSize;
        this.timestamps = new ArrayList<>();
    }

    public long getLastRefillTimestamp() {
        return lastRefillTimestamp;
    }

    public void setLastRefillTimestamp(long lastRefillTimestamp) {
        this.lastRefillTimestamp = lastRefillTimestamp;
    }

    public double getCurrentBucketSize() {
        return currentBucketSize;
    }

    public void setCurrentBucketSize(double currentBucketSize) {
        this.currentBucketSize = currentBucketSize;
    }

    public List<Long> getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(List<Long> timestamps) {
        this.timestamps = timestamps;
    }
}
