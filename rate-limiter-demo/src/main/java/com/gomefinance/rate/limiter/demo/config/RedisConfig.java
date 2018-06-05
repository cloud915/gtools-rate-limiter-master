package com.gomefinance.rate.limiter.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * Created by Administrator on 2018/6/5.
 */
@Configuration
public class RedisConfig {


    /*@Bean
    public StringRedisTemplate stringRedisTemplate(){
        return null;
    }*/

    @Bean
    public DefaultRedisScript<Long> redisScript() {
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/script1.lua")));
        redisScript.setResultType(Long.class);
        return redisScript;
    }
}
