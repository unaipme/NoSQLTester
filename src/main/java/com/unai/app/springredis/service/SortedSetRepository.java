package com.unai.app.springredis.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Repository;

@Repository
public class SortedSetRepository {
	
	private RedisTemplate<String, Object> redisTemplate;
	private ZSetOperations<String, Object> zsetOperations;
	
	@Autowired
	public SortedSetRepository(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	@PostConstruct
	private void init() {
		zsetOperations = redisTemplate.opsForZSet();
	}
	
	public Set<Object> zrange(String key, Long start, Long end) {
		return zsetOperations.range(key, start, end);
	}
	
	public Set<Object> zrange(String key) {
		return zsetOperations.range(key, 0, -1);
	}
	
	public Long zadd(String key, String value, Double score) {
		Set<TypedTuple<Object>> tuples = new HashSet<>();
		TypedTuple<Object> t = new DefaultTypedTuple<Object>(value, score);
		tuples.add(t);
		return zsetOperations.add(key, tuples);
	}
	
	public Long zadd(String key, Map<String, Double> map) {
		Set<TypedTuple<Object>> tuples = new HashSet<>();
		for (Map.Entry<String, Double> e : map.entrySet()) {
			TypedTuple<Object> t = new DefaultTypedTuple<Object>(e.getKey(), e.getValue());
			tuples.add(t);
		}
		return zsetOperations.add(key, tuples);
	}
	
	public Long zrem(String key, Object [] fields) {
		return zsetOperations.remove(key, fields);
	}
	
}
