package com.gomefinance.ratelimiter.config.redisconfig;

/**
 * Created by Administrator on 2017/4/25.
 */

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Data's Redis support.
 *
 * @author Dave Syer
 * @author Andy Wilkinson
 * @author Christian Dupuis
 * @author Christoph Strobl
 * @author Phillip Webb
 * @author Eddú Meléndez
 */
@Configuration
@ConditionalOnClass({ JedisConnection.class, RedisOperations.class, Jedis.class })
@EnableConfigurationProperties
public class RedisAutoConfiguration {



    @Conditional(FileCondition.class)
    @Bean(name = "org.springframework.autoconfigure.redis.RedisProperties")
    @ConditionalOnMissingBean
    public RedisProperties redisProperties(RedisProperties redisProperties) {
        return redisProperties;
    }

    @Conditional(ClassPathCondition.class)
    @Bean(name = "org.springframework.autoconfigure.redis.RedisProperties")
    @ConditionalOnMissingBean
    public RedisProperties redisProperties2(RedisPropertiesClasspath redisPropertiesClasspath) {
        return new RedisProperties(redisPropertiesClasspath);
    }
    /**
     * Base class for Redis configurations.
     */
    protected static abstract class AbstractRedisConfiguration {

        @Autowired
        protected RedisProperties properties;

        @Autowired(required = false)
        private RedisSentinelConfiguration sentinelConfiguration;

        protected final JedisConnectionFactory applyProperties(
                JedisConnectionFactory factory) {
            factory.setHostName(this.properties.getHost());
            factory.setPort(this.properties.getPort());
            if (this.properties.getPassword() != null) {
                factory.setPassword(this.properties.getPassword());
            }
            factory.setDatabase(this.properties.getDatabase());
            if (this.properties.getTimeout() > 0) {
                factory.setTimeout(this.properties.getTimeout());
            }
            return factory;
        }

        protected final RedisSentinelConfiguration getSentinelConfig() {
            if (this.sentinelConfiguration != null) {
                return this.sentinelConfiguration;
            }
            RedisProperties.Sentinel sentinelProperties = this.properties.getSentinel();
            if (sentinelProperties != null) {
                RedisSentinelConfiguration config = new RedisSentinelConfiguration();
                config.master(sentinelProperties.getMaster());
                config.setSentinels(createSentinels(sentinelProperties));
                return config;
            }
            return null;
        }

        private List<RedisNode> createSentinels(RedisProperties.Sentinel sentinel) {
            List<RedisNode> sentinels = new ArrayList<RedisNode>();
            String nodes = sentinel.getNodes();
            for (String node : StringUtils.commaDelimitedListToStringArray(nodes)) {
                try {
                    String[] parts = StringUtils.split(node, ":");
                    Assert.state(parts.length == 2, "Must be defined as 'host:port'");
                    sentinels.add(new RedisNode(parts[0], Integer.valueOf(parts[1])));
                }
                catch (RuntimeException ex) {
                    throw new IllegalStateException(
                            "Invalid redis sentinel " + "property '" + node + "'", ex);
                }
            }
            return sentinels;
        }

    }

    /**
     * Redis connection configuration.
     */
    @Configuration
    @ConditionalOnMissingClass("org.apache.commons.pool2.impl.GenericObjectPool")
    protected static class RedisConnectionConfiguration
            extends AbstractRedisConfiguration {

        @Bean
        @ConditionalOnMissingBean(RedisConnectionFactory.class)
        public JedisConnectionFactory redisConnectionFactory()
                throws UnknownHostException {
            return applyProperties(new JedisConnectionFactory(getSentinelConfig()));
        }

    }

    /**
     * Redis pooled connection configuration.
     */
    @Configuration
    @ConditionalOnClass(GenericObjectPool.class)
    protected static class RedisPooledConnectionConfiguration
            extends AbstractRedisConfiguration {

        @Bean
        @ConditionalOnMissingBean(RedisConnectionFactory.class)
        public JedisConnectionFactory redisConnectionFactory()
                throws UnknownHostException {
            return applyProperties(createJedisConnectionFactory());
        }

        private JedisConnectionFactory createJedisConnectionFactory() {
            if (this.properties.getPool() != null) {
                return new JedisConnectionFactory(getSentinelConfig(), jedisPoolConfig());
            }
            return new JedisConnectionFactory(getSentinelConfig());
        }

        private JedisPoolConfig jedisPoolConfig() {
            JedisPoolConfig config = new JedisPoolConfig();
            RedisProperties.Pool props = this.properties.getPool();
            config.setMaxTotal(props.getMaxActive());
            config.setMaxIdle(props.getMaxIdle());
            config.setMinIdle(props.getMinIdle());
            config.setMaxWaitMillis(props.getMaxWait());
            return config;
        }

    }

    /**
     * Standard Redis configuration.
     */
    @Configuration
    protected static class RedisConfiguration {

        @Bean
        @ConditionalOnMissingBean(name = "redisTemplate")
        public RedisTemplate<Object, Object> redisTemplate(
                RedisConnectionFactory redisConnectionFactory)
                throws UnknownHostException {
            RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
            template.setConnectionFactory(redisConnectionFactory);
            return template;
        }

        @Bean
        @ConditionalOnMissingBean(StringRedisTemplate.class)
        public StringRedisTemplate stringRedisTemplate(
                RedisConnectionFactory redisConnectionFactory)
                throws UnknownHostException {
            StringRedisTemplate template = new StringRedisTemplate();
            template.setConnectionFactory(redisConnectionFactory);
            return template;
        }

    }

}
