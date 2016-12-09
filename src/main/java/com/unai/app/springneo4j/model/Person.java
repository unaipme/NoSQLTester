package com.unai.app.springneo4j.model;

import java.util.List;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

@NodeEntity
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Person {
	
	@GraphId private Long id;
	private String name;
	private Integer born;	

	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type="ACTED_IN", direction=Relationship.OUTGOING)
	private List<Movie> acted_in;
	
	public Person(){}
	
	public Person(String name) {
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getBorn() {
		return born;
	}

	public void setBorn(Integer born) {
		this.born = born;
	}

	public List<Movie> getActed_in() {
		return acted_in;
	}

	public void setActed_in(List<Movie> acted_in) {
		this.acted_in = acted_in;
	}
	
}
