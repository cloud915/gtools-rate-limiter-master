package com.gomefinance.ratelimiter.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by Administrator on 2018/4/17.
 */
@SpringBootApplication
@EnableAutoConfiguration
@ImportResource(locations = {"classpath:applicationContext.xml"})
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
