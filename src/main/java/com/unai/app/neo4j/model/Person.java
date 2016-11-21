package com.unai.app.neo4j.model;

import java.time.Year;
import java.util.HashMap;
import java.util.HashSet;

import org.neo4j.driver.v1.types.Node;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;

public class Person {
	
	private Year born;
	private String name;
	
	@JsonInclude(Include.NON_NULL)
	@JsonManagedReference
	private HashMap<RelationType, HashSet<Movie>> relations = new HashMap<RelationType, HashSet<Movie>>();
	
	protected Person() {}
	
	public Person(String name) {
		this.name = name;
	}
	
	public Person(String name, Year born) {
		this.name = name;
		this.born = born;
	}

	public Year getBorn() {
		return born;
	}

	public void setBorn(Year born) {
		this.born = born;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Person did(RelationType r, Movie m) {
		relations.putIfAbsent(r, new HashSet<Movie>());
		relations.get(r).add(m);
		return this;
	}
	
	public Person did(RelationType r, Movie m, boolean updateMovie) {
		if (updateMovie) {
			m.doneBy(r, this);
		}
		return did(r, m);
	}
	
	public static Person fromNode(Node personNode) {
		Person person = new Person(personNode.get("name").asString());
		if (personNode.containsKey("born")) {
			person.setBorn(Year.of(personNode.get("born").asInt()));
		}
		return person;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (!(o instanceof Person)) return false;
		Person p = (Person) o;
		return p.getName() == getName();
	}
	
}
