package com.unai.app.neo4j.model;

import java.time.Year;
import java.util.HashMap;
import java.util.HashSet;

import org.neo4j.driver.v1.types.Node;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Movie {
	
	private String tagline;
	private String title;
	private Year released;
	
	@JsonInclude(Include.NON_NULL)
	@JsonBackReference
	private HashMap<RelationType, HashSet<Person>> relations = new HashMap<RelationType, HashSet<Person>>();
	
	protected Movie() {}
	
	public Movie(String title) {
		this.title = title;
	}
	
	public Movie(String title, Year released) {
		this.title = title;
		this.released = released;
	}
	
	public Movie(String title, String tagline) {
		this.title = title;
		this.tagline = tagline;
	}
	
	public Movie(String title, String tagline, Year released) {
		this.title = title;
		this.tagline = tagline;
		this.released = released;
	}

	public String getTagline() {
		return tagline;
	}

	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Year getReleased() {
		return released;
	}

	public void setReleased(Year released) {
		this.released = released;
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
			movie.setReleased(Year.of(movieNode.get("released").asInt()));
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
	
}
