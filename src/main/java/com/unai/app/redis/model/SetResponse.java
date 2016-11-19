package com.unai.app.redis.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.unai.app.redis.exception.SetEmptyException;

public class SetResponse extends HashSet<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key;
	
	public SetResponse(String key) {
		this.key = key;
	}
	
	public SetResponse(String key, Set<? extends Object> set) throws SetEmptyException {
		this.key = key;
		if (set == null || set.isEmpty()) {
			throw new SetEmptyException(key);
		}
		cast(set);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public void cast(Set<? extends Object> set) {
		Iterator<? extends Object> it = set.iterator();
		while (it.hasNext()) {
			add(it.next().toString());
		}
	}
	
}
