package com.unai.app.springredis.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ListRepository {
	
	private RedisTemplate<String, Object> redisTemplate;
	private ListOperations<String, Object> listOperations;
	
	@Autowired
	public ListRepository(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	@PostConstruct
	private void init() {
		listOperations = redisTemplate.opsForList();
	}
	
	public Long lpush(String key, String[] value) {
		Long ret = 0L;
		for (String s : value) {
			ret = listOperations.leftPush(key, s);
		}
		return ret;
	}
	
	public Object lindex(String key, Long index) {
		return listOperations.index(key, index);
	}
	
	public List<Object> lall(String key) {
		return listOperations.range(key, 0, listOperations.size(key));
	}
	
	public void lrem(String key) {
		redisTemplate.delete(key);
	}
	
	public void lrem(String key, String field) {
		listOperations.remove(key, 0, field);
	}
	
	public void lrem(String key, String field, Long count) {
		listOperations.remove(key, count, field);
	}
	
}
