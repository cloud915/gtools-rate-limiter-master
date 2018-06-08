package com.gomefinance.rate.limiter.demo.service;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/6/5.
 */
@Service
public class DemoService {


    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    //@Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DefaultRedisScript<Long> redisScript;
    @Autowired
    private DefaultRedisScript<Long> redisScript3;

    public String setSth(String key, String val) {
        stringRedisTemplate.opsForValue().set(key, val, 60, TimeUnit.SECONDS);
        String res = stringRedisTemplate.opsForValue().get(key);
        return res;
    }

    public String getSth(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    private static final String KEY = "simplewindow:";
    private static final String METHOD = "acquire";

    /**
     * 时间窗
     * @param key
     * @param limit
     * @return
     */
    public boolean execScript1(String key, Long limit) {

        //stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        //stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        Long evalResult = stringRedisTemplate.execute(redisScript,
                Collections.singletonList(KEY + key),
                String.valueOf("acquire"),  // key
                String.valueOf(limit),      // 阈值
                String.valueOf(300));       // 过期时间

        return evalResult.longValue()==1L;
    }

    public boolean execScript1ByTimeStampKey() {
        // 从redis服务器获取时间戳，避免由于服务器时间不一致出现问题
        Long currMillSecond = stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.time();
            }
        });
        System.out.println("timestamp:" + (currMillSecond / 1000L));
        return execScript1(String.valueOf(currMillSecond / 1000L), 10L);
    }

    /**
     * 令牌桶
     * @param apps
     * @param key
     * @param permits
     * @return
     */
    public boolean execScript3(String apps, String key, Integer permits) {
        // 取redis服务器的时间戳，防止服务器间时间不一致出现偏差
        Long currMillSecond = stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.time();
            }
        });
        System.out.println("currMillSecond:" + currMillSecond / 1000);
        Long evalResult = stringRedisTemplate.execute(redisScript3,
                ImmutableList.of(key),
                "acquire",
                permits.toString(),     // 取令牌数量
                currMillSecond.toString(),  //当前时间
                apps);    // app标识

        return evalResult.longValue()==1L;
    }

    public boolean execScript3Init(String apps, String key, Integer maxPermits, Integer rate) {
        try {
            Long acquire = stringRedisTemplate.execute(redisScript3,
                    ImmutableList.of(key),
                    "init",
                    maxPermits.toString(),
                    rate.toString(),
                    apps);
            return acquire.longValue()==1L;

        } catch (Exception ex) {
            System.out.println("init rate limit token from redis error,key=" + key);
            return false;
        }
    }

}
