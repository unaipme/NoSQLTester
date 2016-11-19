package com.unai.app.springredis.service;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HashRepository {
	
	private RedisTemplate<String, Object> redisTemplate;
	private HashOperations<String, Object, Object> hashOperations;
	
	@Autowired
	public HashRepository(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}
	
	public Map<Object, Object> hgetall(String key) {
		return hashOperations.entries(key);
	}
	
	public void hmset(String key, Map<Object, Object> map) {
		hashOperations.putAll(key, map);
	}
	
	public void hset(String key, String field, String value) {
		hashOperations.put(key, field, value);
	}
	
	public Object hget(String key, String field) {
		return hashOperations.get(key, field);
	}
	
	public void hdel(String key, String field) {
		hashOperations.delete(key, field);
	}
	
}
