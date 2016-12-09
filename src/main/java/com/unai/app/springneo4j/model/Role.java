package com.unai.app.springneo4j.model;

import java.util.Set;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

@RelationshipEntity(type="ACTED_IN")
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Role {
	
	@GraphId @JsonIgnore private Long id;
	
	@StartNode
	private Person person;
	
	@EndNode @JsonIgnore
	private Movie movie;
	
	private Set<String> roles;
	
	public Role(){}
	
	public Role(Person p, Movie m) {
		this.person = p;
		this.movie = m;
	}

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
