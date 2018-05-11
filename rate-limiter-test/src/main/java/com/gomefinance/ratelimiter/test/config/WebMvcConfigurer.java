package com.gomefinance.ratelimiter.test.config;

import com.gomefinance.ratelimiter.test.constants.RateLimiterConstants;
import com.gomefinance.ratelimiter.toolkit.RateLimiterToolkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/27.
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);
    private String pathPattern = "/my/*";

    @Autowired
    private RateLimiterToolkit rateLimiterToolkit;

    /*@Value("${rate.limiter.redis.host}")
    private String rateLimiterRedisHost;*/

    /* public WebMvcConfigurer() {
         logger.info("[print]" + rateLimiterRedisHost);
     }

     public WebMvcConfigurer(String pathPattern) {
         this.pathPattern = pathPattern;
     }*/

    private static boolean FLAG_INIT = false;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                boolean token = false;
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Method method = handlerMethod.getMethod();
                RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

                if (rateLimiter != null && requestMapping != null) {
                    int maxPermits = rateLimiter.maxPermits();
                    int rate = rateLimiter.rate();
                    String url = requestMapping.value()[0];
                    //logger.info("[rateLimiterRedisHost]-->" + rateLimiterRedisHost);
                    if (FLAG_INIT == false) {
                        FLAG_INIT = rateLimiterToolkit.init(RateLimiterConstants.APP,
                                RateLimiterConstants.getKey("", url),
                                maxPermits, rate);
                    }

                    token = rateLimiterToolkit.acquire(RateLimiterConstants.APP,
                            RateLimiterConstants.getKey("", url), 1);

                    if (token == false) {
                        response.sendError(500, "[gome finance] Restricting access");
                        return false;
                    }
                    logger.info("token-->{},url-->{},time-->{}", token, url, new Date().getSeconds());
                }
                return true;
            }
        }).addPathPatterns(this.pathPattern);

    }


}
