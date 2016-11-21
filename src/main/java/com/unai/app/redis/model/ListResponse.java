package com.unai.app.redis.model;

import java.util.ArrayList;
import java.util.List;

import com.unai.app.redis.exception.ListEmptyException;

public class ListResponse {
	
	private String key;
	private List<String> values;
	
	public ListResponse(String key, List<? extends Object> values) throws ListEmptyException {
		this.key = key;
		if (values == null || values.isEmpty()) {
			throw new ListEmptyException(key);
		}
		cast(values);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}
	
	public void add(String value) {
		values.add(value);
	}
	
	private void cast(List<? extends Object> list) {
		this.values = new ArrayList<String>();
		for (Object o : list) {
			values.add(o.toString());
		}
	}
	
}
