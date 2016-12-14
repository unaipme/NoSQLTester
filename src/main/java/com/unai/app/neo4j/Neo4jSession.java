package com.unai.app.neo4j;

import java.io.Closeable;
import java.util.Map;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Statement;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.types.TypeSystem;

public class Neo4jSession implements Session, Closeable {
	
	private Driver driver;
	private Session session;
	
	private final static String username = System.getenv("NEO4J_BOLT_USER");
	private final static String password = System.getenv("NEO4J_BOLT_PASSWORD");
	private final static String uri = System.getenv("NEO4J_BOLT_URL");
	
	public Neo4jSession() {
		driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
		session = driver.session();
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
