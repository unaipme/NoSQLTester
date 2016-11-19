package com.unai.app.redis.exception;

public class SetEmptyException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SetEmptyException(String s) {
		super(String.format("Set for key %s is empty", s));
	}
	
}
