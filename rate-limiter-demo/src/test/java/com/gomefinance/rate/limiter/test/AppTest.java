package com.gomefinance.rate.limiter.test;

import com.gomefinance.rate.limiter.demo.DemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by Administrator on 2018/6/5.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
@WebAppConfiguration
public class AppTest {
    @Test
    public void theTest() {
        System.out.println(12222222);
    }



}
