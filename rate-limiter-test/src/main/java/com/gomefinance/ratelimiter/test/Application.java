package com.gomefinance.ratelimiter.test;

import com.gomefinance.ratelimiter.test.service.MyService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2018/4/17.
 */
@SpringBootApplication
@ImportResource(locations = {"classpath:applicationContext.xml"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        //ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        //String message = ctx.getBean("message", String.class);
        //System.out.println("message-->" + message);

        //MyService myService = ctx.getBean("myService", MyService.class);
        //myService.checkBean();
    }

}
