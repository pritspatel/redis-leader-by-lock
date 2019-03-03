package com.pritspatel.leaderbylock.config;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


@Configuration
@ImportResource({"classpath*:intg-ctx.xml"})
public class RedisConfig {
	
	@Bean
	public JedisPool jedisPool() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestWhileIdle(true);
		return new JedisPool(poolConfig, "localhost", 6379, 500);
	}
	
	@Bean
	public Jedis jedis(JedisPool jedisPool) {
		return jedisPool.getResource();
	}
	
	@Bean
	public String myApplicationId() {
		return UUID.randomUUID().toString();
	}
	
}
