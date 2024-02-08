package org.rateLimiter.service;

import org.rateLimiter.policy.Bucket;

interface IRedisService {
    boolean add(String key, Bucket bucket);

    Bucket get(String key);
}
