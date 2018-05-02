package com.gomefinance.ratelimiter.web.constants;

/**
 * Created by Administrator on 2018/4/27.
 */
public class RateLimiterConstants {

    public static final String APP = "gomefinance.rate.limiter.web";
    private static final String KEY = "config.finance";

    public static String getKey(String controller, String action) {
        StringBuffer sb = new StringBuffer(KEY);
        sb.append(".");
        sb.append(controller);
        sb.append(".");
        sb.append(action);
        return sb.toString();
    }
}
