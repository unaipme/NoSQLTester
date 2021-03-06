package com.unai.app.springneo4j.model;

import java.util.List;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

@NodeEntity
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Movie {
	
	@GraphId @JsonIgnore private Long id;
	private String title;
	private Integer released;
	private String tagline;
	
	@JsonInclude(Include.NON_EMPTY)
	@Relationship(type="ACTED_IN", direction=Relationship.INCOMING)
	private List<Role> cast;
	
	public Movie() {}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getReleased() {
		return released;
	}

	public void setReleased(Integer released) {
		this.released = released;
	}

	public String getTagline() {
		return tagline;
	}

	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	public List<Role> getCast() {
		return cast;
	}

	public void setCast(List<Role> cast) {
		this.cast = cast;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
