package com.unai.app.springneo4j.model;

import java.util.Set;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unai.app.neo4j.model.Movie;

@RelationshipEntity(type="ACTED_IN")
public class Role {
	
	@GraphId @JsonIgnore private Long id;
	
	@StartNode
	private Person person;
	
	@EndNode
	private Movie movie;
	
	private Set<String> roles;
	
	public Role(){}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	
}
