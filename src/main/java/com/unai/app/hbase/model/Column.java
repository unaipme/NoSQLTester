package com.unai.app.hbase.model;

import java.util.Map;
import java.util.TreeMap;

public class Column {
	
	private String qualifier;
	private TreeMap<Long, String> values = new TreeMap<>();
	
	public Column(String qualifier) {
		this.qualifier = qualifier;
	}
	
	public Column(String qualifier, String value, Long timestamp) {
		this.qualifier = qualifier;
		put(timestamp, value);
	}
	
	public String getQualifier() {
		return qualifier;
	}
	
	public String put(Long key, String value) {
		return values.put(key, value);
	}
	
	public Map<Long, String> getValues() {
		return values;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Column) || o == null) return false;
		Column c = (Column) o;
		if (c.getQualifier().equals(getQualifier())) return true;
		return false;
	}
	
}
