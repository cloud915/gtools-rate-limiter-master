package com.gomefinance.ratelimiter.config.redisconfig;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by Administrator on 2017/4/20.
 */
public class ClassPathCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String s=ClassPathCondition.class.getResource("ClassPathCondition.class").toString();
        return !s.contains("jar:");
//        ClassPathResource classPathResource=new ClassPathResource("mybatis.properties");
//        return classPathResource.exists();
    }
}
