package com.unai.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
public class RedisConfig {
	
	private static final String host = System.getenv("REDIS_URL");
	private static final Integer port = Integer.parseInt(System.getenv("REDIS_PORT"));
	
	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory f = new JedisConnectionFactory();
		f.setHostName(host);
		f.setPort(port);
		return f;
	}
	
	@Primary
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		final RedisTemplate<String, Object> t = new RedisTemplate<String, Object>();
		t.setConnectionFactory(jedisConnectionFactory());
		//t.setKeySerializer(new StringRedisSerializer());
		//t.setHashValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		t.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		return t;
	}

}
