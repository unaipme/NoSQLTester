package com.unai.app.redis;

import com.unai.app.redis.model.HashResponse;
import com.unai.app.redis.model.KeyValueResponse;

import redis.clients.jedis.Jedis;

public class JedisDriver extends Jedis {
	
	private static final String host = System.getenv("REDISCLOUD_URL");
	
	public JedisDriver() {
		super(host);
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
