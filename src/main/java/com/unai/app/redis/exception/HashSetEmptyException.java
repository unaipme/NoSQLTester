package com.unai.app.redis.exception;

public class HashSetEmptyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7818519353185960157L;
	
	public HashSetEmptyException(String s) {
		super("Hash empty for key " + s);
	}
}
