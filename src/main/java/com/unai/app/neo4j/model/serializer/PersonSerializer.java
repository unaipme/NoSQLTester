package com.unai.app.neo4j.model.serializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.unai.app.neo4j.model.Movie;
import com.unai.app.neo4j.model.Person;
import com.unai.app.neo4j.model.RelationType;

public class PersonSerializer extends StdSerializer<Person> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PersonSerializer() {
		super(Person.class);
	}

	@Override
	public void serialize(Person p, JsonGenerator generator, SerializerProvider sp) throws IOException {
		generator.writeStartObject();
		generator.writeStringField("name", p.getName());
		generator.writeNumberField("born", p.getBorn());
		generator.writeObjectFieldStart("filmography");
		for (Map.Entry<RelationType, Set<Movie>> r : p.getRelations().entrySet()) {
			generator.writeArrayFieldStart(r.getKey().name());
			Iterator<Movie> it = r.getValue().iterator();
			while (it.hasNext()) {
				Movie m = it.next();
				generator.writeStartObject();
				generator.writeStringField("title", m.getTitle());
				generator.writeStringField("tagline", m.getTagline());
				generator.writeNumberField("released", m.getReleased());
				generator.writeEndObject();
			}
			generator.writeEndArray();
		}
		generator.writeEndObject();
		generator.writeEndObject();
	}
	
}
