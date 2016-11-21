package com.unai.app.utils;

import java.net.URI;

import org.springframework.http.HttpHeaders;

public class HTTPHeaders extends HttpHeaders {

	/**
	 * 
	 */
	private static final long serialVersionUID = 603398629093456615L;
	
	public HTTPHeaders location(String uri) {
		setLocation(URI.create(uri));
		return this;
	}

}
