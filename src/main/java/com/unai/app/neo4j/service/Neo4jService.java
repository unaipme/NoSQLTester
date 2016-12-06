package com.unai.app.neo4j.service;

import java.util.Set;

import org.neo4j.driver.v1.StatementResult;

import com.unai.app.neo4j.exception.NotEnoughInformationException;
import com.unai.app.neo4j.model.Properties;

public interface Neo4jService<T> {
	
	public static final String isNumeric = "[-+]?[0-9]*\\.?[0-9]+?";
	
	public Set<T> getAll(StatementResult sr);
	
	public T getOne(StatementResult sr);
	
	public String createQuery(T data) throws NotEnoughInformationException;
	
	public String deleteQuery(Properties properties);
	
	public String selectWhereQuery(Properties properties);
	
	public String updateQuery(String where, String set);
	
}
