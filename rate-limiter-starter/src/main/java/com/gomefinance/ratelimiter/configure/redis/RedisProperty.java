package com.gomefinance.ratelimiter.configure.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/5/10.
 */
@Configuration
public class RedisProperty {

    @Value("${rate.limiter.redis.host}")
    private String host;
    @Value("${rate.limiter.redis.port}")
    private Integer port;
    @Value("${rate.limiter.redis.password}")
    private String password;
    @Value("${rate.limiter.redis.maxIdle}")
    private Integer maxIdle;
    @Value("${rate.limiter.redis.minIdle}")
    private Integer minIdle;
    @Value("${rate.limiter.redis.maxWaitMillis}")
    private Integer maxWaitMillis;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Integer getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(Integer maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }
}
