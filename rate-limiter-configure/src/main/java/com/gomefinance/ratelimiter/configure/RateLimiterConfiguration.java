package com.gomefinance.ratelimiter.configure;

import com.gomefinance.ratelimiter.toolkit.RateLimiterToolkit;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
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
@ImportResource(locations = {"classpath*:applicationContext-rateLimiter.xml"})
public class RateLimiterConfiguration {

    private StringRedisTemplate stringRedisTemplate;

    public RateLimiterConfiguration(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 预加载脚本
     * @return
     */
    private DefaultRedisScript<Long> rateLimiterLua() {
        DefaultRedisScript<Long> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setLocation(new ClassPathResource("classpath:rateLimiter_token_bucket.lua"));
        defaultRedisScript.setResultType(Long.class);
        return defaultRedisScript;
    }

    /**
     * 如果配置文件中没有注入RateLimiterToolkit
     * 则使用该Bean进行初始化
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "rateLimiterToolkit")
    public RateLimiterToolkit rateLimiterToolkit() {
        return new RateLimiterToolkit(stringRedisTemplate, rateLimiterLua());
    }

    /*@Bean
    @ConditionalOnMissingBean(name="springRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(){

    }*/

}
