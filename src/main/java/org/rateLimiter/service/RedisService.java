package org.rateLimiter.service;

import org.rateLimiter.policy.Bucket;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisService implements IRedisService {
    private JedisPool jedisPool;

    public RedisService(JedisPool jedisPooll) {
        this.jedisPool = new JedisPool(new JedisPoolConfig(), "localhost");
    }

    @Override
    public boolean add(String key, Bucket bucket) {
        return false;
    }

    @Override
    public void remove(String key, Bucket bucket) {

    }
}
