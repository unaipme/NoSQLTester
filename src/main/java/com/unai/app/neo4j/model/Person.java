package com.unai.app.neo4j.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.driver.v1.types.Node;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.unai.app.neo4j.model.serializer.PersonSerializer;

@JsonSerialize(using = PersonSerializer.class)
public class Person {
	
	private Integer born;
	private String name;
	
	private HashMap<RelationType, Set<Movie>> relations = new HashMap<>();
	
	protected Person() {}
	
	public Person(String name) {
		this.name = name;
	}
	
	public Person(String name, Integer born) {
		this.name = name;
		this.born = born;
	}

	public Integer getBorn() {
		return (born == null)?0:born;
	}

	public void setBorn(Integer born) {
		this.born = born;
	}

	public String getName() {
		return (name == null)?"":name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public HashMap<RelationType, Set<Movie>> getRelations() {
		return relations;
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
			person.setBorn(personNode.get("born").asInt());
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
	
	public boolean nameIsNull() {
		return name == "" || name == null;
	}
	
	public boolean bornIsNull() {
		return born == 0 || born == null;
	}
	
}
