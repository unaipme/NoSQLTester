package com.unai.app.redis.model;

import java.util.HashMap;
import java.util.Map;

import com.unai.app.redis.exception.HashSetEmptyException;

public class HashResponse extends HashMap<String, String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2088355616481785375L;
	private String key;
	
	public HashResponse(String key, Map<? extends Object, ? extends Object> map) throws HashSetEmptyException {
		this.key = key;
		if (map == null || map.isEmpty()) {
			throw new HashSetEmptyException(key);
		}
		cast(map);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	private void cast(Map<? extends Object, ? extends Object> map) {
		for (Map.Entry<? extends Object, ? extends Object> entry : map.entrySet()) {
			put(entry.getKey().toString(), entry.getValue().toString());
		}
	}

}
