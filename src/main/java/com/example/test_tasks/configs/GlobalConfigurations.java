package com.example.test_tasks.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class GlobalConfigurations {

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory){

        RedisTemplate<String,Object> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());

        GenericJacksonJsonRedisSerializer serializer =  GenericJacksonJsonRedisSerializer.builder()
                .enableSpringCacheNullValueSupport()
                .build();

        template.setValueSerializer(serializer);

        template.afterPropertiesSet();

        return template;
    }

    @Bean
    public PasswordEncoder encoder(){

        return new BCryptPasswordEncoder();
    }

}
