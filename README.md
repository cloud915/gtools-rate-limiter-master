# gtools-rate-limiter-master
限流组件

集成tookit时，需要增加配置applicationContext.xml，
<import resource="applicationContext-rateLimiter.xml"/>
<bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="location" value="classpath:/ratelimiter-redis.properties"/>
</bean>

其中引用配置文件ratelimiter-redis.properties必填内容如下：
rate.limiter.redis.host=****
rate.limiter.redis.port=6379
rate.limiter.redis.pass=****
