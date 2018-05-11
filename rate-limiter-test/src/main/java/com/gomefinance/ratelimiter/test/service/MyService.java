package com.gomefinance.ratelimiter.test.service;

import com.gomefinance.ratelimiter.toolkit.RateLimiterToolkit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Administrator on 2018/5/10.
 */
@Service
public class MyService {

    @Autowired
    @Qualifier("message")
    private String message;

    @Autowired
    @Qualifier("tempMessage")
    private String tempMessage;

    @Autowired
    private String age;

    @Autowired
    private RateLimiterToolkit rateLimiterToolkit;


    public void checkBean() {

        System.out.println("====begin=====");
        //System.out.println("the bean is null? -->" + (rateLimiterToolkit == null));
        //System.out.println("the stringRedisTemplate is null? -->" + (stringRedisTemplate == null));
        System.out.println("message-->" + message);
        System.out.println("tempMessage-->" + tempMessage);
        System.out.println("age-->" + age);
        System.out.println("====end=====");

        boolean res = rateLimiterToolkit.init("appsss", "keysss", 10, 1);
        System.out.println("init rateLimitToolkit-->" + res);
    }

    public String index() {
        return "the date is -->" + System.currentTimeMillis();
    }
}
