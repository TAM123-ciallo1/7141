package com.qin.switch705.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 设置键序列化器
        template.setKeySerializer(new StringRedisSerializer());
        // 设置值序列化器（JSON格式，支持对象序列化）
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 初始化参数和方法
        template.afterPropertiesSet();
        return template;
    }
}
