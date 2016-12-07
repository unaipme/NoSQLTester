package com.unai.app.springneo4j.model;

import java.util.List;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NodeEntity
public class Person {
	
	@GraphId private Long id;
	private String name;
	private Integer born;	

	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type="ACTED_IN")
	private List<Movie> acted_in;

	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type="DIRECTED")
	private List<Movie> directed;
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type="PRODUCED")
	private List<Movie> produced;
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type="WROTE")
	private List<Movie> wrote;
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type="REVIEWED")
	private List<Movie> reviewed;
	
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

	public List<Movie> getDirected() {
		return directed;
	}

	public void setDirected(List<Movie> directed) {
		this.directed = directed;
	}

	public List<Movie> getProduced() {
		return produced;
	}

	public void setProduced(List<Movie> produced) {
		this.produced = produced;
	}

	public List<Movie> getWrote() {
		return wrote;
	}

	public void setWrote(List<Movie> wrote) {
		this.wrote = wrote;
	}

	public List<Movie> getReviewed() {
		return reviewed;
	}

	public void setReviewed(List<Movie> reviewed) {
		this.reviewed = reviewed;
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
