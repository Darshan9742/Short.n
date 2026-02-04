package com.darshan.url_shorten_v5.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;

@Service
public class RedisTokenBucketService {
    private static final String PREFIX = "tb:";

    private final StringRedisTemplate stringRedisTemplate;
    private final DefaultRedisScript<Long> tokenBucketScript;

    @Autowired
    public RedisTokenBucketService(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
        this.tokenBucketScript = new DefaultRedisScript<>();
        this.tokenBucketScript.setLocation(new ClassPathResource("token_bucket.lua"));
        this.tokenBucketScript.setResultType(Long.class);
    }

    public boolean allow(String key, int capacity, double refillRatePerSec, int cost){
        String redisKey = PREFIX + key;
        long now = Instant.now().getEpochSecond();

        Long result = stringRedisTemplate.execute(
                tokenBucketScript,
                Collections.singletonList(redisKey),
                String.valueOf(capacity),
                String.valueOf(refillRatePerSec),
                String.valueOf(now),
                String.valueOf(cost)
        );

        return result != null && result == 1;
    }
}
