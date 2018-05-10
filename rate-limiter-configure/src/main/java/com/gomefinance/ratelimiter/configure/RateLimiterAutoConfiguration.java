package com.gomefinance.ratelimiter.configure;

import com.gomefinance.ratelimiter.toolkit.RateLimiterToolkit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * Created by Administrator on 2018/4/17.
 */
@Configuration
@AutoConfigureBefore(RedisAutoConfiguration.class)
@ConditionalOnBean(StringRedisTemplate.class)
public class RateLimiterAutoConfiguration {
    //private StringRedisTemplate stringRedisTemplate;

    /*public RateLimiterAutoConfiguration(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }*/

    private DefaultRedisScript<Long> rateLimiterLua() {
        DefaultRedisScript<Long> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setLocation(new ClassPathResource("classpath:rateLimiter_token_bucket.lua"));
        defaultRedisScript.setResultType(Long.class);
        return defaultRedisScript;
    }

    /*@Bean
    @ConditionalOnMissingBean(name = "rateLimiterToolkit")
    public RateLimiterToolkit rateLimiterToolkit() {
        return new RateLimiterToolkit(stringRedisTemplate, rateLimiterLua());
    }*/

}
