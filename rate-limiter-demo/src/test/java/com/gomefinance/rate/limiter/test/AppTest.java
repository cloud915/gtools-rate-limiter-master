package com.gomefinance.rate.limiter.test;

import com.gomefinance.rate.limiter.demo.DemoApplication;
import com.gomefinance.rate.limiter.demo.service.DemoService;
import com.google.common.util.concurrent.RateLimiter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Administrator on 2018/6/5.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
public class AppTest {

    @Autowired
    private DemoService demoService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Before
    public void before() {
        demoService.setStringRedisTemplate(stringRedisTemplate);
    }

    @Test
    public void test2() throws InterruptedException {
        long a = System.currentTimeMillis();
        RateLimiter limiter = RateLimiter.create(5);
        System.out.println();
        System.out.println("time" + (System.currentTimeMillis() - a));
        System.out.println();
        System.out.println(limiter.acquire(1));
        Thread.sleep(100);
        System.out.println(limiter.acquire(1));
        Thread.sleep(100);
        System.out.println(limiter.acquire(1));
        Thread.sleep(100);
        System.out.println(limiter.acquire(1));
        Thread.sleep(1000);
        System.out.println(limiter.acquire(1));
        System.out.println(limiter.acquire(1));
        System.out.println(limiter.acquire(1));
        System.out.println(limiter.acquire(1));
        System.out.println(limiter.acquire(1));
        System.out.println(limiter.acquire(1));
        System.out.println(limiter.acquire(1));

    }

    @Test
    public void theTest() {
        demoService.setSth("theKey", "a02");
        System.out.println("theTest print : " + demoService.getSth("theKey"));
    }

    @Test
    public void execScript1() throws InterruptedException {
        for (int i = 0; i < 15; i++) {
            System.out.println("execScript1 print ：" + demoService.execScript1("rateLimiter1", 10L));
        }
    }

    @Test
    public void execScript1_2() throws InterruptedException {
        for (int i = 0; i < 30; i++) {

            System.out.println("execScript1_2 print ：" + demoService.execScript1ByTimeStampKey());
            if (i > 15) {
                Thread.sleep(200);
            }
        }
    }


    @Test
    public void execScript3() throws InterruptedException {
        String app = "gomeDemo";
        String key = "rateLimiter3";
        int maxPermits = 10; // 允许积压
        int rate = 10;
        //demoService.execScript3Init(app, key, maxPermits, rate);
        for (int i = 0; i < 50; i++) {
            if (i < 10) {
                System.out.println("[" + i + "]execScript3 print ：" + demoService.execScript3(app, key, 1));
                Thread.sleep(100);
            } else if (i < 20) {
                System.out.println("[" + i + "]execScript3 print ：" + demoService.execScript3(app, key, 2));
                Thread.sleep(100);
            } else {
                System.out.println("[" + i + "]execScript3 print ：" + demoService.execScript3(app, key, 5));
                Thread.sleep(100);
            }
        }
    }
}
