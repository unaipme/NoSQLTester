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

public class MovieSerializer extends StdSerializer<Movie> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MovieSerializer() {
		super(Movie.class);
	}

	@Override
	public void serialize(Movie m, JsonGenerator generator, SerializerProvider sp) throws IOException {
		generator.writeStartObject();
		generator.writeStringField("title", m.getTitle());
		generator.writeStringField("tagline", m.getTagline());
		generator.writeNumberField("released", m.getReleased());
		generator.writeObjectFieldStart("crew");
		for (Map.Entry<RelationType, Set<Person>> r : m.getRelations().entrySet()) {
			generator.writeArrayFieldStart(r.getKey().name());
			Iterator<Person> it = r.getValue().iterator();
			while (it.hasNext()) {
				Person p = it.next();
				generator.writeStartObject();
				generator.writeStringField("Name", p.getName());
				generator.writeNumberField("born", p.getBorn());
				generator.writeEndObject();
			}
			generator.writeEndArray();
		}
		generator.writeEndObject();
		generator.writeEndObject();
	}
	
}
