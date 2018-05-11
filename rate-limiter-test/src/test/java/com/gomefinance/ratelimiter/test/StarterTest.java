package com.gomefinance.ratelimiter.test;

import com.gomefinance.ratelimiter.test.service.MyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Administrator on 2018/5/10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class StarterTest {
    @Autowired
    MyService myService;

    @Test
    public void doit() {
        myService.checkBean();
    }
}
