package com.unai.app.springneo4j.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unai.app.springneo4j.model.Movie;
import com.unai.app.springneo4j.model.Person;
import com.unai.app.springneo4j.model.Role;
import com.unai.app.springneo4j.repo.MovieRepository;
import com.unai.app.springneo4j.repo.PersonRepository;

@Service
public class SpringPersonService {
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private MovieRepository movieRepository;
	
	public List<Person> findAll() {
		return personRepository.findAll();
	}
	
	public Person findByName(String name) {
		return personRepository.findByName(name);
	}
	
	public List<Person> findOlderThan(Integer age) {
		return personRepository.findByBornLessThan(LocalDate.now().getYear() - age);
	}
	
	public void save(Person p) {
		personRepository.save(p);
	}
	
	public void delete(Person p) {
		personRepository.delete(p);
	}
	
	public void delete(String name) {
		personRepository.delete(personRepository.findByName(name));
	}
	
	/**
	 * 
	 * @param name The person's name
	 * @param title The movie's title
	 */
	public void newRole(String name, String title, String [] roles) {
		Person p = personRepository.findByName(name);
		Movie m = movieRepository.findByTitle(title);
		Role r = new Role(p, m);
		r.setRoles(new HashSet<>(Arrays.asList(roles)));
		m.getCast().add(r);
		movieRepository.save(m);
	}
	
}
