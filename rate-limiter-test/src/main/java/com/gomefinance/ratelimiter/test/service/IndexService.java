package com.gomefinance.ratelimiter.test.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Created by Administrator on 2018/5/2.
 */
@Service
public class IndexService {

    @HystrixCommand(fallbackMethod = "fallback")
    public String getRateLimiter() {
        StringBuilder sb = new StringBuilder("getData result:");
        sb.append(System.currentTimeMillis());

        return sb.toString();
    }

    @HystrixCommand(fallbackMethod = "fallback")
    public String getRateLimiter2() {
        StringBuilder sb = new StringBuilder("getData2 result:");
        sb.append(System.currentTimeMillis());

        return sb.toString();
    }

    Random random = new Random();
    @HystrixCommand(fallbackMethod = "fallback")
    public String getCircuitBreaker() {
        try {
            int a = random.nextInt(4);
            if (a == 3) {
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder("getData3 result:");
        sb.append(System.currentTimeMillis());

        return sb.toString();
    }

    public String fallback() {
        return "error";
    }
}
