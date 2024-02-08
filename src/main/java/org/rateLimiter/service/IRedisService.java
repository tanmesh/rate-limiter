package org.rateLimiter.service;

import org.rateLimiter.policy.Bucket;

interface IRedisService {
    boolean add(String key, Bucket bucket);

    void remove(String key, Bucket bucket);
}
