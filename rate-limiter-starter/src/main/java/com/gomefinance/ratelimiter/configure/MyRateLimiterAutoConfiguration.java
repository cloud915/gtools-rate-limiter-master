package com.gomefinance.ratelimiter.configure;

import com.gomefinance.ratelimiter.configure.redis.RedisTemplateConfiguration;
import com.gomefinance.ratelimiter.toolkit.RateLimiterToolkit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * Created by Administrator on 2018/5/10.
 */
@Configuration
@Import(RedisTemplateConfiguration.class)
public class MyRateLimiterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name="message")
    public String message(){
        return "the message";
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private DefaultRedisScript<Long> rateLimiterLua;

    @Bean
    @ConditionalOnBean(value = {StringRedisTemplate.class})
    @ConditionalOnMissingBean(name = "rateLimiterToolkit")
    public RateLimiterToolkit rateLimiterToolkit() {
        return new RateLimiterToolkit(stringRedisTemplate, rateLimiterLua);
    }

}
