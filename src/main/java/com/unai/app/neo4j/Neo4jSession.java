package com.unai.app.neo4j;

import java.util.Map;

import javax.annotation.Resource;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Statement;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.types.TypeSystem;

@Resource
public class Neo4jSession implements Session {
	
	private Driver driver;
	private Session session;
	
	private Neo4jSession(String uri, String username, String password) {
		driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
		session = driver.session();
	}
	
	public static Neo4jSession getSession(String uri, String username, String password) {
		return new Neo4jSession(uri, username, password);
	}

	@Override
	public boolean isOpen() {
		return session.isOpen();
	}

	@Override
	public StatementResult run(String arg0) {
		return session.run(arg0);
	}

	@Override
	public StatementResult run(Statement arg0) {
		return session.run(arg0);
	}

	@Override
	public StatementResult run(String arg0, org.neo4j.driver.v1.Value arg1) {
		return session.run(arg0, arg1);
	}

	@Override
	public StatementResult run(String arg0, Map<String, Object> arg1) {
		return session.run(arg0, arg1);
	}

	@Override
	public StatementResult run(String arg0, Record arg1) {
		return session.run(arg0, arg1);
	}

	@Override
	public TypeSystem typeSystem() {
		return session.typeSystem();
	}

	@Override
	public Transaction beginTransaction() {
		return session.beginTransaction();
	}

	@Override
	public void close() {
		session.close();
		driver.close();
	}
	
}
