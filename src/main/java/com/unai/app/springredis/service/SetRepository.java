package com.unai.app.springredis.service;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

@Repository
public class SetRepository {

	private RedisTemplate<String, Object> redisTemplate;
	private SetOperations<String, Object> setOperations;
	
	@Autowired
	public SetRepository(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	@PostConstruct
	private void init() {
		setOperations = redisTemplate.opsForSet();
	}
	
	public Long sadd(String key, Object [] values) {
		return setOperations.add(key, values);
	}
	
	public Set<Object> smembers(String key) {
		return setOperations.members(key);
	}
	
	public void sremall(String key) {
		redisTemplate.delete(key);
	}
	
	public Long srem(String key, Object [] values) {
		return setOperations.remove(key, values);
	}
	
}
