package com.unai.app.springneo4j.repo;

import java.util.List;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.unai.app.springneo4j.model.Person;

public interface PersonRepository extends GraphRepository<Person> {
	
	public Person findByName(String name);
	
	public List<Person> findAll();
	
	public List<Person> findByBornLessThan(Integer born);
	
}
