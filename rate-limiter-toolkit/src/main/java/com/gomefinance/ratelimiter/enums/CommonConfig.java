package com.gomefinance.ratelimiter.enums;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/5/3.
 */
@Component("commonConfig")
public class CommonConfig {

    @Value("${rate.limiter.app}")
    private String app;
    @Value("${rate.limiter.key.prefix}")
    private String keyPrefix;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getKey(String controller, String action) {
        StringBuffer sb = new StringBuffer(keyPrefix);
        sb.append(".");
        sb.append(controller);
        sb.append(".");
        sb.append(action);
        return sb.toString();
    }
}
