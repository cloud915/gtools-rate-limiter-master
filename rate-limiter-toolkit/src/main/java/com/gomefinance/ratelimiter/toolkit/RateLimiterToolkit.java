package com.gomefinance.ratelimiter.toolkit;

import com.gomefinance.ratelimiter.enums.RateLimiterConstants;
import com.gomefinance.ratelimiter.enums.TokenStatus;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ImportResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

/**
 * Created by GENGHONGYU on 2018/4/17.
 */
public class RateLimiterToolkit {

    private Logger logger = LoggerFactory.getLogger(RateLimiterToolkit.class);

    private StringRedisTemplate stringRedisTemplate;
    private RedisScript<Long> rateLimiterScriptLua;

    public RateLimiterToolkit(StringRedisTemplate stringRedisTemplate, RedisScript<Long> rateLimiterScriptLua) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.rateLimiterScriptLua = rateLimiterScriptLua;
    }

    private String getKey(String key) {
        return RateLimiterConstants.RATE_LIMITER_KEY_PREFIX + key;
    }

    public boolean init(String apps, String key, Integer maxPermits, Integer rate) {
        TokenStatus token;
        try {

            /*Long currMillSecond = stringRedisTemplate.execute(new RedisCallback<Long>() {
                @Override
                public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    return redisConnection.time();
                }
            });*/

            Long acquire = stringRedisTemplate.execute(rateLimiterScriptLua,
                    ImmutableList.of(getKey(key)),
                    RateLimiterConstants.RATE_LIMITER_INIT_METHOD,
                    maxPermits.toString(), rate.toString(), apps);

            if (acquire == 1) {
                token = TokenStatus.INIT_SUCCESSFUL;
            } else {
                logger.error("init rate limit config is failed,apps=" + apps + ",key=" + key);
                token = TokenStatus.INIT_FAILED;
            }
        } catch (Exception ex) {
            logger.error("init rate limit token from redis error,key=" + key, ex);
            token = TokenStatus.ACCESS_REDIS_FAIL;
        }
        return token.isInitSuccess();
    }

    public boolean delete(String key) {
        TokenStatus token;
        try {

            Long delte = stringRedisTemplate.execute(rateLimiterScriptLua, ImmutableList.of(getKey(key)),
                    RateLimiterConstants.RATE_LIMITER_DELETE_METHOD);
            if (delte == 1) {
                token = TokenStatus.DELETE_SUCCESSFUL;
            } else {
                logger.error("delete rate limit config is failed,key=" + key);
                token = TokenStatus.DELETE_FAILED;
            }
        } catch (Exception ex) {
            logger.error("delete rate limit token from redis error,key=" + key, ex);
            token = TokenStatus.ACCESS_REDIS_FAIL;
        }
        return token.isDeleteSuccess();
    }

    /**
     * 尝试获取令牌
     *
     * @param context 应用唯一标识
     * @param key     按key进行限流
     * @return
     */
    public boolean acquire(String context, String key, Integer permits) {
        TokenStatus token = permits == null
                ? acquireToken(context, key, 1)
                : acquireToken(context, key, permits);
        return token.isPass() || token.isAccessRedisFail();
    }

    private TokenStatus acquireToken(String context, String key, Integer permits) {
        TokenStatus token;
        try {
            Long currMillSecond = stringRedisTemplate.execute(new RedisCallback<Long>() {
                @Override
                public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    return redisConnection.time();
                }
            });

            Long acquire = stringRedisTemplate.execute(rateLimiterScriptLua,
                    ImmutableList.of(getKey(key)),
                    RateLimiterConstants.RATE_LIMITER_ACQUIRE_METHOD,
                    permits.toString(), currMillSecond.toString(), context);

            if (acquire == 1) {
                token = TokenStatus.PASS;
            } else if (acquire == -1) {
                token = TokenStatus.FUSING;
            } else {
                logger.error("no rate limit config for context={}", context);
                token = TokenStatus.NO_CONFIG;
            }
        } catch (Exception ex) {
            logger.error("get rate limit token from redis error,key=" + key, ex);
            token = TokenStatus.ACCESS_REDIS_FAIL;
        }
        return token;
    }

}
