package com.unai.app.neo4j.model;

import java.util.ArrayList;

public class Properties extends ArrayList<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean fromMatch = false;
	
	private String prefix = "";
	
	private Properties() {}
	
	private Properties(boolean fromMatch) {
		this.fromMatch = fromMatch;
	}
	
	public static Properties createMatchProperties() {
		return new Properties(true);
	}
	
	public static Properties createWhereProperties() {
		return new Properties(false);
	}
	
	public void add(String name, String value) {
		if (fromMatch) {
			add(String.format("%s: '%s'", name, value));
		} else {
			if (prefix != "") add(String.format("%s.%s = '%s'", prefix, name, value));
			else add(String.format("%s = '%s'", name, value));
		}
	}
	
	public void add(String name, Integer value) {
		if (fromMatch) {
			add(String.format("%s: %d", name, value));
		} else {
			if (prefix != "") add(String.format("%s.%s = %d", prefix, name, value));
			else add(String.format("%s = %d", name, value));
		}
	}
	
	public void add(String name, Double value) {
		if (fromMatch) {
			add(String.format("%s: %f", name, value));
		} else {
			if (prefix != "") add(String.format("%s.%s = %f", prefix, name, value));
			else add(String.format("%s = %f", name, value));
		}
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
		for (int i=0; i<size(); i++) {
			String s = get(i);
			if (!s.startsWith(prefix + ".")) {
				if (s.contains(".")) {
					s.replaceFirst(s.substring(0, s.indexOf(".") - 1), prefix);
				} else {
					set(i, String.format("%s.%s", prefix, s));
				}
			}
		}
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String parse() {
		if (fromMatch) {
			return String.format("{%s}", String.join(", ", this));
		} else {
			return String.join(" AND ", this);
		}
	}
	
}
