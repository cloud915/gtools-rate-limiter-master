package test;

import com.gomefinance.ratelimiter.toolkit.RateLimiterToolkit;
import com.gomefinance.ratelimiter.web.Application;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Administrator on 2018/4/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class ToolkitTest {

    @Autowired
    private RateLimiterToolkit rateLimiterToolkit;

    String apps = "gtools.rate.limiter";
    String key = "config.finance";


    @Before
    public void init() {

        try {
            boolean result = rateLimiterToolkit.init(apps, key, 100, 10);
            System.out.println("init result=" + result);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @After
    public void delete() {
        try {
            boolean result = rateLimiterToolkit.delete(key);
            System.out.println("delete result=" + result);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void getPermit() {
        try {
            for (int i = 0; i < 100; i++) {
                boolean result = rateLimiterToolkit.acquire(apps, key, 3);
                System.out.println("acquire i=" + i + "\t\t result =" + result);
                Thread.sleep(50);
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

}
