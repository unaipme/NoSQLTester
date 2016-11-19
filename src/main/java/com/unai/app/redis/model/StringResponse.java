package com.unai.app.redis.model;

import com.unai.app.redis.exception.KeyValueNotFoundException;

public class StringResponse {
	
	private String key;
	private String value;
	
	public StringResponse(String key, String value) throws KeyValueNotFoundException {
		this.key = key;
		if (value == null) {
			throw new KeyValueNotFoundException(key);
		}
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}