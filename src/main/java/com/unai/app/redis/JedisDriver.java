package com.unai.app.redis;

import com.unai.app.redis.model.HashResponse;
import com.unai.app.redis.model.KeyValueResponse;

import redis.clients.jedis.Jedis;

public class JedisDriver extends Jedis {
	
	public JedisDriver(String host, final int port) {
		super(host, port);
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
