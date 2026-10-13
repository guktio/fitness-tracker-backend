package com.fitness.application.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RedisLogger {

    private static final Logger log = LoggerFactory.getLogger(RedisLogger.class);

    @Value("${spring.data.redis.url:redis://localhost:6379}")
    private String redisUrl;

    @EventListener(ApplicationReadyEvent.class)
    public void logRedisUrl() {
        log.info("=================================================");
        log.info("Connected to Redis URL: {}", redisUrl);
        log.info("=================================================");
    }
}