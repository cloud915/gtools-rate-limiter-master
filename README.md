# gtools-rate-limiter-master
限流组件

集成tookit时，需要增加配置applicationContext.xml，
```java
<import resource="applicationContext-rateLimiter.xml"/>
<bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="location" value="classpath:/ratelimiter-redis.properties"/>
</bean>
```

其中引用配置文件ratelimiter-redis.properties必填内容如下：
```properties
rate.limiter.redis.host=1.2.3.4
rate.limiter.redis.port=6379
rate.limiter.redis.pass=1234
```


限流注解描述：
```java
@RateLimiter(maxPermits="最大令牌数",rate="流速，个/秒")
```

