package com.unai.app.config;

import java.util.Arrays;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
@EnableMongoRepositories(basePackages = "com.unai.app.springmongo.repo")
public class MongoConfig extends AbstractMongoConfiguration {
	
	private final String username = System.getenv("MONGODB_USER");
	private final String host = System.getenv("MONGODB_URI");
	private final String password = System.getenv("MONGODB_PASSWORD");
	private final Integer port = Integer.parseInt(System.getenv("MONGODB_PORT"));
	private final String db = System.getenv("MONGODB_DB");

	@Override
	protected String getDatabaseName() {
		return db;
	}

	@Override
	public Mongo mongo() throws Exception {
		ServerAddress address = new ServerAddress(host, port);
		MongoCredential credential = MongoCredential.createCredential(username, db, password.toCharArray());
		return new MongoClient(address, Arrays.asList(credential));
	}
	
	@Override
	protected String getMappingBasePackage() {
		return "com.unai.app.springmongo.model";
	}
	
}
