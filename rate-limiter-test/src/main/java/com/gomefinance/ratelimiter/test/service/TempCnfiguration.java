package com.gomefinance.ratelimiter.test.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2018/5/10.
 */
@Configuration
public class TempCnfiguration {
    @Bean
    public String age(){
        return "18";
    }
    @Bean(name = "tempMessage")
    public String message(){
        return "the tempMessage";
    }
}
