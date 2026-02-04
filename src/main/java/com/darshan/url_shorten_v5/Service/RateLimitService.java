package com.darshan.url_shorten_v5.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

    private final RedisTokenBucketService redisTokenBucketService;

    @Autowired
    public RateLimitService(RedisTokenBucketService redisTokenBucketService) {
        this.redisTokenBucketService = redisTokenBucketService;
    }

    public boolean allowRequest(String ip, String path) {
        if (path.startsWith("/api/shorten")){
            return redisTokenBucketService.allow(
                    ip + ":shorten",
                    6,
                    0.5, //0.15tokens per second
                    1
            );
        }
        else if(path.startsWith("/api/")){
            return redisTokenBucketService.allow(
                    ip + ":redirect",
                    60,
                    0.50,
                    1
            );
        }
        else{
            return true;
        }
    }
}