package com.fitness.application.cahce;

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
import com.fitness.application.gym.plan.PlansDTO;
import com.fitness.application.gym.workout.WorkoutsDTO;


@Configuration
public class RedisConfig {

    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory cf) {
        RedisCacheConfiguration base = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .disableCachingNullValues();

        return RedisCacheManager.builder(cf)
                .cacheDefaults(base)
                .withCacheConfiguration("exercise",
                        configFor(ExercisesDTO.Exercise.class, Duration.ofMinutes(30)))
                .withCacheConfiguration("plan",
                        configFor(PlansDTO.Plan.class, Duration.ofMinutes(30)))
                .withCacheConfiguration("workout",
                                configFor(WorkoutsDTO.Workout.class, Duration.ofMinutes(60)))
                .withCacheConfiguration("workoutInfo",
                        configFor(WorkoutsDTO.Info.class, Duration.ofMinutes(30)))
                .withCacheConfiguration("workoutExercis",
                        configFor(WorkoutsDTO.WorkoutExercise.class, Duration.ofMinutes(30)))
                .withCacheConfiguration("workout",
                        configFor(PlansDTO.Plan.class, Duration.ofMinutes(30)))
                .build();
    }

    private <T> RedisCacheConfiguration configFor(Class<T> type, Duration ttl) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new JacksonJsonRedisSerializer<>(type)));
    }
}