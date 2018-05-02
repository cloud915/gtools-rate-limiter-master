package com.gomefinance.ratelimiter.web.config;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2018/4/27.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    /**
     * 最大令牌数量
     * @return
     */
    int maxPermits() default 5;

    /**
     * 单位时间
     * @return
     */
    int rateTime() default 1000;
}

