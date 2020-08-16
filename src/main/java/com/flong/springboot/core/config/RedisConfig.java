package com.flong.springboot.core.config;

import com.flong.springboot.core.util.EntityRedisSerializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;


@Configuration
public class RedisConfig {
    private static final Log log = LogFactory.getLog(RedisConfig.class);

    // Redis服务器地址
    @Value("${redis.host}")
    private String host;
    // Redis服务器连接端口
    @Value("${redis.port}")
    private int port;
    // Redis服务器连接密码（默认为空）
    @Value("${redis.password}")
    private String password;
    // 连接超时时间（毫秒）
    @Value("${redis.timeout}")
    private int timeout;
    // 连接超时时间（毫秒）
    @Value("${redis.database}")
    private int database;
    // 连接池最大连接数（使用负值表示没有限制）
    @Value("${redis.pool.max-active}")
    private int maxTotal;
    // 连接池最大阻塞等待时间（使用负值表示没有限制）
    @Value("${redis.pool.max-wait}")
    private int maxWaitMillis;
    // 连接池中的最大空闲连接
    @Value("${redis.pool.max-idle}")
    private int maxIdle;
    // 连接池中的最小空闲连接
    @Value("${redis.pool.min-idle}")
    private int minIdle;


    /**
     * 配置JedisPoolConfig
     *
     * @return JedisPoolConfig实体
     */
    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig jedisPoolConfig() {
        log.info("初始化JedisPoolConfig");
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 连接池最大连接数（使用负值表示没有限制）
        jedisPoolConfig.setMaxTotal(this.maxTotal);
        // 连接池最大阻塞等待时间（使用负值表示没有限制）
        jedisPoolConfig.setMaxWaitMillis(this.maxWaitMillis);
        // 连接池中的最大空闲连接
        jedisPoolConfig.setMaxIdle(this.maxIdle);
        // 连接池中的最小空闲连接
        jedisPoolConfig.setMinIdle(this.minIdle);
        //        jedisPoolConfig.setTestOnBorrow(true);
        //        jedisPoolConfig.setTestOnCreate(true);
        //        jedisPoolConfig.setTestWhileIdle(true);
        return jedisPoolConfig;
    }

    /**
     * 实例化 RedisConnectionFactory 对象
     *
     * @param poolConfig
     * @return
     */
    @Bean(name = "jedisConnectionFactory")
    public RedisConnectionFactory jedisConnectionFactory(@Qualifier(value = "jedisPoolConfig") JedisPoolConfig poolConfig) {
        log.info("初始化RedisConnectionFactory");
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
        jedisConnectionFactory.setHostName(this.host);
        jedisConnectionFactory.setPort(this.port);
        jedisConnectionFactory.setDatabase(this.database);
        return jedisConnectionFactory;
    }

    /**
     * 实例化 RedisTemplate 对象
     *
     * @return
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, String> functionDomainRedisTemplate(@Qualifier(value = "jedisConnectionFactory") RedisConnectionFactory factory) {
        log.info("初始化RedisTemplate");
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new EntityRedisSerializer());
        redisTemplate.setValueSerializer(new EntityRedisSerializer());
        redisTemplate.afterPropertiesSet();
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }
}