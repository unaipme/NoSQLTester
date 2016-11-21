package com.unai.app.springredis.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HyperLogLogRepository {
	
	private RedisTemplate<String, Object> redisTemplate;
	private HyperLogLogOperations<String, Object> hllOperations;
	
	@Autowired
	public HyperLogLogRepository(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	@PostConstruct
	private void init() {
		hllOperations = redisTemplate.opsForHyperLogLog();
	}
	
	public Long pfadd(String key, Object [] elems) {
		return hllOperations.add(key, elems);
	}
	
	public Long pfcount(String key) {
		return hllOperations.size(key);
	}
	
	public void pfmerge(String destKey, String sourceKey) {
		hllOperations.union(destKey, sourceKey);
	}
	
}
