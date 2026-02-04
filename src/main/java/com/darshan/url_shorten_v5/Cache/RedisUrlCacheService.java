package com.darshan.url_shorten_v5.Cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisUrlCacheService {
    private static final String PREFIX = "url:";

    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisUrlCacheService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getUrl(String shortUrl){
        Object val = redisTemplate.opsForValue().get(PREFIX + shortUrl);
        return val != null ? val.toString() : null;
    }

    public void putUrl(String shortUrl, String originalUrl, Duration ttl){
        redisTemplate.opsForValue().set(PREFIX + shortUrl, originalUrl, ttl);
    }

    public void evict(String shortUrl){
        redisTemplate.delete(PREFIX + shortUrl);
    }
}
