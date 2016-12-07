package com.unai.app.springneo4j.repo;

import java.util.List;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.unai.app.springneo4j.model.Movie;

public interface MovieRepository extends GraphRepository<Movie> {
	
	public Movie findByTitle(String title);
	
	public List<Movie> findAll();
	
}
