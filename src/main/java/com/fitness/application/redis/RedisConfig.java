package com.fitness.application.redis;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration 
public class RedisConfig {

    // TODO: GenericJackson2JsonRedisSerializer replace with GenericJacksonJsonRedisSerializer
    @Bean
    public CacheManager redisCacheManager() {
        RedisSerializationContext.SerializationPair<Object> jsonSerializer = 
         RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());
         return RedisCacheManager.builder()
                .cacheDefaults(
                    RedisCacheConfiguration.defaultCacheConfig()
                            .entryTtl(Duration.ofDays(1))
                            .serializeValuesWith(jsonSerializer)
                )
                .build();
    }

}
