package com.unai.app.mongo.model;

import org.bson.Document;
import org.bson.conversions.Bson;

public interface Documentable {
	
	public Document toDocument();
	public Bson updater();
	
}
