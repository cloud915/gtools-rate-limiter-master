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

import javax.xml.bind.SchemaOutputResolver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/6/5.
 */
@Service
public class DemoService {


    @Autowired
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

    public boolean execScript(String key, Long limit) {

        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        Long evalResult = stringRedisTemplate.execute(redisScript,
                Collections.singletonList(KEY + key),
                String.valueOf("acquire"),  // key
                String.valueOf(limit),      // 阈值
                String.valueOf(300));       // 过期时间

        return evalResult.equals(1L);
    }

    public boolean execScriptByFixedKey() {

        return execScript("simplewindow", 10L);
    }

    public boolean execScriptByTImeStampKey() {
        // 从redis服务器获取时间戳，避免由于服务器时间不一致出现问题
        Long currMillSecond = stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.time();
            }
        });
        System.out.println("timestamp:" + (currMillSecond / 1000L));
        return execScript(String.valueOf(currMillSecond / 1000L), 10L);
    }

    public boolean execScript2(int count,int rateCount,long rateTime){
        StringBuffer redisKey = new StringBuffer();
        redisKey.append("slideWindowRateLimiter");

        Long evalResult = stringRedisTemplate.execute(redisScript,
                Collections.singletonList(redisKey.toString()),
                String.valueOf(count),  //申请发送的数量 1
                String.valueOf(rateCount),//阀值数量 2
                String.valueOf(rateTime),//阀值时间（毫秒）3
                String.valueOf(System.currentTimeMillis()));//申请的时间4

        return evalResult.equals(count);
    }

    public boolean execScript3(String key, Integer permits){
        // 取redis服务器的时间戳，防止服务器间时间不一致出现偏差
        Long currMillSecond = stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.time();
            }
        });

        Long evalResult = stringRedisTemplate.execute(redisScript3,
                ImmutableList.of(key),
                "acquire",
                permits.toString(),     // 令牌数量
                currMillSecond.toString(),
                "gomeDemo");

        return evalResult.equals(1L);
    }

}
