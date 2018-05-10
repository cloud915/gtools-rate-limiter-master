package com.gomefinance.ratelimiter.test.controller;

import com.gomefinance.ratelimiter.config.RateLimiter;
import com.gomefinance.ratelimiter.test.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/4/27.
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @RateLimiter(maxPermits = 1,rateTime = 2)
    @RequestMapping(value = "/getRateLimiter", method = RequestMethod.GET)
    public String getRateLimiter() {
       return indexService.getRateLimiter();
    }

    @RateLimiter(maxPermits = 1,rateTime = 4)
    @RequestMapping(value = "/getRateLimiter2", method = RequestMethod.GET)
    public String getRateLimiter2() {
        return indexService.getRateLimiter2();
    }

    //@RateLimiter(maxPermits = 1,rateTime = 5)
    @RequestMapping(value = "/getCircuitBreaker", method = RequestMethod.GET)
    public String getCircuitBreaker() {
        return indexService.getCircuitBreaker();
    }
}
