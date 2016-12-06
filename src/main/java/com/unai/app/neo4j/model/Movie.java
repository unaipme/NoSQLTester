package com.unai.app.neo4j.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.driver.v1.types.Node;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.unai.app.neo4j.model.serializer.MovieSerializer;

@JsonSerialize(using = MovieSerializer.class)
public class Movie {
	
	private String tagline;
	private String title;
	private Integer released;
	
	private HashMap<RelationType, Set<Person>> relations = new HashMap<>();
	
	protected Movie() {}
	
	public Movie(String title) {
		this.title = title;
	}
	
	public Movie(String title, Integer released) {
		this.title = title;
		this.released = released;
	}
	
	public Movie(String title, String tagline) {
		this.title = title;
		this.tagline = tagline;
	}
	
	public Movie(String title, String tagline, Integer released) {
		this.title = title;
		this.tagline = tagline;
		this.released = released;
	}

	public String getTagline() {
		return (tagline == null)?"":tagline;
	}

	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	public String getTitle() {
		return (title == null)?"":title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getReleased() {
		return (released==null)?0:released;
	}

	public void setReleased(Integer released) {
		this.released = released;
	}
	
	public HashMap<RelationType, Set<Person>> getRelations() {
		return relations;
	}

	public Movie doneBy(RelationType r, Person p) {
		relations.putIfAbsent(r, new HashSet<Person>());
		relations.get(r).add(p);
		return this;
	}
	
	public Movie doneBy(RelationType r, Person p, boolean updatePerson) {
		if (updatePerson) {
			p.did(r, this);
		}
		return doneBy(r, p);
	}
	
	public static Movie fromNode(Node movieNode) {
		Movie movie = new Movie(movieNode.get("title").asString());
		if (movieNode.containsKey("released")) {
			movie.setReleased(movieNode.get("released").asInt());
		}
		if (movieNode.containsKey("tagline")) {
			movie.setTagline(movieNode.get("tagline").asString());
		}
		return movie;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (!(o instanceof Movie)) return false;
		Movie m = (Movie) o;
		return m.getTitle() == getTitle() && m.getReleased() == getReleased();
	}
	
	public boolean titleIsNull() {
		return title == "" || title == null;
	}
	
	public boolean taglineIsNull() {
		return tagline == "" || tagline == null;
	}
	
	public boolean releasedIsNull() {
		return released == 0 || released == null;
	}
	
}
