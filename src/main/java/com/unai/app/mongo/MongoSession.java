package com.unai.app.mongo;

import java.io.Closeable;
import java.util.Arrays;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoSession implements Closeable {
	
	private final static String username = System.getenv("MONGODB_USER");
	private final static String host = System.getenv("MONGODB_URI");
	private final static String password = System.getenv("MONGODB_PASSWORD");
	private final static Integer port = Integer.parseInt(System.getenv("MONGODB_PORT"));
	private final static String db = System.getenv("MONGODB_DB");
	
	private MongoClient client;
	private MongoDatabase mongodb;
	private MongoCollection<Document> collection;
	
	private MongoSession(MongoClient client) {
		this.client = client;
		mongodb = client.getDatabase(db);
		collection = mongodb.getCollection("restaurants");
	}
	
	public static MongoSession create() {
		MongoCredential credential = MongoCredential.createCredential(username, db, password.toCharArray());
		ServerAddress server = new ServerAddress(host, port);
		MongoClient client = new MongoClient(server, Arrays.asList(credential));
		return new MongoSession(client);
	}
	
	public MongoClient getClient() {
		return client;
	}
	
	public MongoDatabase getDB() {
		return mongodb;
	}
	
	public MongoCollection<Document> getCollection() {
		return collection;
	}

	@Override
	public void close() {
		client.close();
	}

}
