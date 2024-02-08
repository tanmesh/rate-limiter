package org.rateLimiter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.rateLimiter.policy.Bucket;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RedisService implements IRedisService {
    private JedisPool jedisPool;
    private ObjectMapper objectMapper;

    public RedisService() {
        this.objectMapper = new ObjectMapper();
        this.jedisPool = new JedisPool(new JedisPoolConfig(), "localhost");
    }

    @Override
    public boolean add(String key, Bucket bucket) {
        try (Jedis jedis = jedisPool.getResource()) {
            String agentJson = objectMapper.writeValueAsString(bucket);
            int ttlInSeconds = 60 * 60 * 24; // 24 hr
            System.out.println("key: " + key);
            System.out.println("agentJson: " + agentJson);
            jedis.setex(key, ttlInSeconds, agentJson);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Bucket get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            String tmp = jedis.get(key);
            return objectMapper.readValue(tmp, Bucket.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
