package com.hupu.deep.comment.web.config;

import com.hupu.foundation.cache.CacheClient;
import com.hupu.foundation.cache.CountClient;
import com.hupu.foundation.cache.FoundationCacheConfig;
import com.hupu.foundation.util.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.SnappyCodecV2;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiangfangyuan, mayifan0701
 * @since 2019-07-20 18:27
 */
@Configuration
public class CacheConfig {
    @Value("${spring.redis.host:10.64.56.159}")
    private String redisHost;
    @Value("${spring.redis.port:6379}")
    private String redisPort;
    @Value("${spring.redis.password:mypassword}")
    private String redisPassword;
    @Value("${spring.redis.database:0}")
    private Integer database;
    @Value("${spring.redis.timeout:9000}")
    private Integer timeout;

    @Autowired
    private FoundationCacheConfig foundationCacheConfig;

    @Bean
    public RedissonClient redissonClient() {

        AssertUtil.notBlank(redisHost);
        AssertUtil.notBlank(redisPort);

        if(Boolean.valueOf(System.getProperty("local"))){
            redisHost = "192.168.43.229";
        }

        Config config = new Config();
        if (StringUtils.isNotBlank(redisPassword)) {
            config.useSingleServer()
                    .setAddress(String.format("redis://%s:%s", redisHost, redisPort))
                    .setPassword(redisPassword)
                    .setDatabase(database)
                    .setTimeout(timeout)
                    .setTcpNoDelay(true);
        } else {
            config.useSingleServer()
                    .setAddress(String.format("redis://%s:%s", redisHost, redisPort))
                    .setDatabase(database)
                    .setTimeout(timeout)
                    .setTcpNoDelay(true);
        }
        config.setCodec(new SnappyCodecV2(new JsonJacksonCodec()));
        config.setNettyThreads(32);
        return Redisson.create(config);
    }

    @Bean
    public CacheClient cacheClient() {
        return new CacheClient(redissonClient(),foundationCacheConfig);
    }


    @Bean
    public CountClient countClient() {
        return new CountClient(redissonClient(),foundationCacheConfig);
    }
}
