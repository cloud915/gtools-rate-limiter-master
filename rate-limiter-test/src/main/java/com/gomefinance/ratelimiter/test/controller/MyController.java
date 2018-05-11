package com.gomefinance.ratelimiter.test.controller;

import com.gomefinance.ratelimiter.test.config.RateLimiter;
import com.gomefinance.ratelimiter.test.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/5/11.
 */
@RestController
@RequestMapping("/my")
public class MyController {

    @Autowired
    private MyService myService;

    @RequestMapping("/index")
    @RateLimiter(maxPermits = 5, rate = 2)
    public String index() {
        return myService.index();
    }
}
