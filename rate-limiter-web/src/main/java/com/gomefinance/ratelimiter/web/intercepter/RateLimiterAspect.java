package com.gomefinance.ratelimiter.web.intercepter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/4/27.
 */
//@Component
//@Aspect
public class RateLimiterAspect {

    public RateLimiterAspect() {
    }

    //@Pointcut("execution(com.gomefinance.ratelimiter.web.controller.*(..))")
    public void pointcut() {

    }

    //@Around("pointcut()")
    public void around(ProceedingJoinPoint pjp) {
        // 由于已经进入

    }
}
