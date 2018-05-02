package com.gomefinance.ratelimiter.enums;

import org.omg.PortableInterceptor.SUCCESSFUL;

/**
 * Created by GENGHONGYU on 2018/4/17.
 */
public enum TokenStatus {
    /**
     * 表示获取token成功，通过
     */
    PASS,
    /**
     * 表示没有配置元数据
     */
    NO_CONFIG,
    /**
     * 表示获取token失败，熔断
     */
    FUSING,
    /**
     * 表示访问redis出问题了
     */
    ACCESS_REDIS_FAIL,
    /**
     * 初始化限流参数，成功
     */
    INIT_SUCCESSFUL,
    /**
     * 初始化限流参数，失败
     */
    INIT_FAILED,
    /**
     * 删除限流参数，成功
     */
    DELETE_SUCCESSFUL,
    /**
     * 删除限流参数，失败
     */
    DELETE_FAILED;

    public boolean isPass() {
        return this.equals(PASS);
    }

    public boolean isFusing() {
        return this.equals(FUSING);
    }

    public boolean isAccessRedisFail() {
        return this.equals(ACCESS_REDIS_FAIL);
    }

    public boolean isNoConfig() {
        return this.equals(NO_CONFIG);
    }

    public boolean isInitSuccess() {
        return this.equals(INIT_SUCCESSFUL);
    }

    public boolean isDeleteSuccess() {
        return this.equals(DELETE_SUCCESSFUL);
    }

}
