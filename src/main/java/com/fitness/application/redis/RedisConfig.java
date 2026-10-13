package com.fitness.application.redis;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import com.fitness.application.gym.exercises.dto.ExercisesDTO;


@Configuration 
public class RedisConfig {

    // TODO: GenericJackson2JsonRedisSerializer replace with GenericJacksonJsonRedisSerializer
    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory cf) {
        var serializer = new JacksonJsonRedisSerializer<>(ExercisesDTO.Exercise.class); // в Boot 4; Jackson2... deprecated

        RedisCacheConfiguration cfg = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(serializer));

        return RedisCacheManager.builder(cf)
                .cacheDefaults(cfg)
                .build();
    }

}
