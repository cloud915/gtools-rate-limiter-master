package com.gomefinance.ratelimiter.web.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Administrator on 2018/4/27.
 */
public class ApplicationListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationListener.class);

    //@Autowired
    //private RateLimiterToolkit rateLimiterToolkit;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //rateLimiterToolkit.init()
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
            logger.info(">>>>>>>>>contextDestroyed");
    }
}
