package com.gomefinance.rate.limiter.test;

import com.gomefinance.rate.limiter.demo.DemoApplication;
import com.gomefinance.rate.limiter.demo.service.DemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/6/5.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
public class AppTest {

    @Autowired
    private DemoService demoService;

    @Test
    public void theTest() {
        demoService.setSth("theKey", "a02");
        System.out.println("theTest print : " + demoService.getSth("theKey"));
    }

    @Test
    public void execScript1() throws InterruptedException {
        for (int i = 0; i < 15; i++) {
            System.out.println("execScript print ：" + demoService.execScript("fixed", 10L));
        }
    }

    @Test
    public void execScript2() throws InterruptedException {
        for (int i = 0; i < 30; i++) {

            System.out.println("execScript print ：" + demoService.execScriptByTImeStampKey());
            if (i > 15) {
                Thread.sleep(200);
            }
        }
    }


    @Test
    public void execScript3() throws InterruptedException {
        for (int i = 0; i < 30; i++) {

            System.out.println("execScript print ：" + demoService.execScript3("rateLimit3",1));
            if (i > 15) {
                Thread.sleep(200);
            }
        }
    }
}
