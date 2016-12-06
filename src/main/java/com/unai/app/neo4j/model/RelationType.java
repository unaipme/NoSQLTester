package com.unai.app.neo4j.model;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.driver.v1.types.Relationship;

public enum RelationType {
	DIRECTED,
	PRODUCED,
	ACTED_IN,
	REVIEWED,
	FOLLOWS,
	WROTE;
	
	private HashMap<String, Object> properties;
	
	RelationType() {
		properties = new HashMap<String, Object>();
	}
	
	public Object getProperty(String key) {
		return properties.get(key);
	}
	
	public void setProperty(String key, Object value) {
		properties.put(key, value);
	}
	
	public boolean hasProperties() {
		return !properties.isEmpty();
	}
	
	public static RelationType fromRelationship(Relationship node) {
		RelationType rt = valueOf(node.type());
		for (Map.Entry<String, Object> e : node.asMap().entrySet()) {
			rt.setProperty(e.getKey(), e.getValue());
		}
		return rt;
	}
}
