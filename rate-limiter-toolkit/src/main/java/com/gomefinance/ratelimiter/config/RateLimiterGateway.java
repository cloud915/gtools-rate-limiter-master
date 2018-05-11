package com.gomefinance.ratelimiter.config;

import com.gomefinance.ratelimiter.toolkit.RateLimiterToolkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Administrator on 2018/4/27.
 */
@Component
public class RateLimiterGateway {
    private static final Logger logger = LoggerFactory.getLogger(RateLimiterGateway.class);
    private String pathPattern = "/my/*";
    private static boolean FLAG_INIT = false;
    @Autowired
    private RateLimiterToolkit rateLimiterToolkit;

    public boolean getToken(RateLimiter rateLimiter, String app, String key) {
        boolean token = false;

        if (rateLimiter != null && key != null) {
            int maxPermits = rateLimiter.maxPermits();
            int rate = rateLimiter.rate();
            //logger.info("[rateLimiterRedisHost]-->" + rateLimiterRedisHost);
            if (FLAG_INIT == false) {
                FLAG_INIT = rateLimiterToolkit.init(app, key, maxPermits, rate);
            }

            token = rateLimiterToolkit.acquire(app, key, 1);

            //logger.info("token-->{},key-->{},time-->{}", token, key, new Date().getSeconds());
        }
        return token;
    }

}
