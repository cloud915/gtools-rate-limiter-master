package com.gomefinance.ratelimiter.test.config;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2018/5/11.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    /**
     * 最大令牌数量
     * 当maxPermits>rate时，闲时会累积令牌，进而允许单位时间内容流量突发maxPermits个
     * 当maxPermits<=rate时，请求量与rate设置成正比，不允许有流量突发
     * @return
     */
    int maxPermits() default 5;

    /**
     * 令牌放入速率，单位：个/秒
     * @return
     */
    int rate() default 1;


}
