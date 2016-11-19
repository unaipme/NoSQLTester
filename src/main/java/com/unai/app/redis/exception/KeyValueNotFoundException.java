package com.unai.app.redis.exception;

public class KeyValueNotFoundException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5360998752741681360L;

	public KeyValueNotFoundException(String s) {
		super("Value not found for key " + s);
	}
	
}
