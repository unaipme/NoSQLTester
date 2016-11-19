package com.unai.app.springredis.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class ValueRepository {
	
	private RedisTemplate<String, Object> redisTemplate;
	private ValueOperations<String, Object> valueOperations;
	
	@Autowired
	public ValueRepository(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	@PostConstruct
	private void init() {
		valueOperations = redisTemplate.opsForValue();
	}
	
	public Object get(String key) {
		return valueOperations.get(key);
	}
	
	public void set(String key, Object value) {
		valueOperations.set(key, value);
	}
	
	public void del(String key) {
		redisTemplate.delete(key);
	}
	
}
