package com.unai.app.redis.exception;

public class ListEmptyException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4841231983791472637L;

	public ListEmptyException(String key) {
		super(String.format("List is empty for key %s", key));
	}

}
