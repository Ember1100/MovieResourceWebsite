package com.panda.framework.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>() ;
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        //定义序列化方式 JSON
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper() ;
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY) ;

        jackson2JsonRedisSerializer.setObjectMapper(om);

        //专门序列化字符串的（明文）
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer() ;

        //设置 RedisTemplate 的序列化方式
        // 设置 String 类型的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        //设置 hash, list ,set,zset
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return  redisTemplate ;
    }
}
