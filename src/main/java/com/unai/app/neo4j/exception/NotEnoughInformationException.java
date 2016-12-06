package com.unai.app.neo4j.exception;

public class NotEnoughInformationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotEnoughInformationException(String s) {
		super(String.format("Not enough information to create %s", s));
	}

}
