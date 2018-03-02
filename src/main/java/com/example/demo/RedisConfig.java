package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

  @Value("${jedis.host:local}")
  private String jedisHost;

  @Bean
  public JedisConnectionFactory jedisConnectionFactory() {
    return new JedisConnectionFactory();
  }

  @Bean
  JedisPool jedisPool() {
    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setTestOnBorrow(true);
    poolConfig.setTestWhileIdle(true);
    return new JedisPool(poolConfig, jedisHost, 6379, 60000);
  }

}
