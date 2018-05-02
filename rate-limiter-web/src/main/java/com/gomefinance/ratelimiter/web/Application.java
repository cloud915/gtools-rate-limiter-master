package com.gomefinance.ratelimiter.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Administrator on 2018/4/27.
 */
@SpringBootApplication
@EnableCircuitBreaker
@ImportResource(locations = {"classpath:applicationContext.xml"})
//@PropertySource("classpath:ratelimiter-redis.properties")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
