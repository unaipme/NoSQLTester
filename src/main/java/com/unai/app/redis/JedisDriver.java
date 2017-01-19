package com.unai.app.redis;

import com.unai.app.redis.model.HashResponse;
import com.unai.app.redis.model.KeyValueResponse;

import redis.clients.jedis.Jedis;

public class JedisDriver extends Jedis {
	
	private static final String host = System.getenv("REDIS_URL");
	private static final Integer port = Integer.parseInt(System.getenv("REDIS_PORT"));
	
	public JedisDriver() {
		super(String.format("redis://%s:%d", host, port));
	}
	
	public String set(KeyValueResponse s) {
		return super.set(s.getKey(), s.getValue());
	}
	
	public String hmset(HashResponse hr) {
		return super.hmset(hr.getKey(), hr);
	}
	
	public Long hset(String key, KeyValueResponse s) {
		return super.hset(key, s.getKey(), s.getValue());
	}
	
}
