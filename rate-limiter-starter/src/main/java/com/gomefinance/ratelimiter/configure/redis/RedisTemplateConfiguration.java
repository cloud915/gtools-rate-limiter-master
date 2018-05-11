package com.gomefinance.ratelimiter.configure.redis;

import com.gomefinance.ratelimiter.enums.RateLimiterConstants;
import com.gomefinance.ratelimiter.toolkit.RateLimiterToolkit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Administrator on 2018/5/10.
 */
@Configuration
@Import(RedisProperty.class)
//@EnableConfigurationProperties(RateLimiterRedisProperties.class)
public class RedisTemplateConfiguration {

    @Autowired
    private RedisProperty redisProperty;
    //@Autowired
    //private RateLimiterRedisProperties rateLimiterRedisProperties;

    @Bean
    @ConditionalOnMissingBean(name = "stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate() {

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisProperty.getMaxIdle());
        poolConfig.setMinIdle(redisProperty.getMinIdle());
        poolConfig.setMaxWaitMillis(redisProperty.getMaxWaitMillis());

        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setPoolConfig(poolConfig);
        factory.setHostName(redisProperty.getHost());
        factory.setPort(redisProperty.getPort());
        factory.setPassword(redisProperty.getPassword());
        factory.afterPropertiesSet();

        StringRedisTemplate template = new StringRedisTemplate(factory);
        template.afterPropertiesSet();

        return template;
    }

    @Bean
    public DefaultRedisScript<Long> rateLimiterLua() {
        DefaultRedisScript<Long> defaultRedisScript = new DefaultRedisScript<>();
        //defaultRedisScript.setLocation(RateLimiterConstants.LUA_SCRIPT_RESOURCE);
        defaultRedisScript.setScriptText(RateLimiterConstants.LUA_SCRIPT_TEXT);
        defaultRedisScript.setResultType(Long.class);
        return defaultRedisScript;
    }

}
