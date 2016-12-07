package com.unai.app.springneo4j.rest;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unai.app.springneo4j.model.Person;
import com.unai.app.springneo4j.repo.PersonRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/sneo4j/people")
public class SpringNeo4jPersonController {
	
	private Logger log = LoggerFactory.getLogger(SpringNeo4jPersonController.class);
	
	@Autowired
	private PersonRepository personRepository;
	
	@GetMapping("/")
	public List<Person> getAll() {
		return personRepository.findAll();
	}
	
	@GetMapping("/{name}")
	public ResponseEntity<?> getOne(@PathVariable String name) {
		try {
			Person p = personRepository.findByName(name);
			if (p == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				return ResponseEntity.ok(p);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/olderthan/{age}")
	public List<Person> getOlderThan(@PathVariable Integer age) {
		return personRepository.findByBornLessThan(LocalDate.now().getYear() - age);
	}
	
	@PostMapping(value="/people", consumes={"application/json"})
	public ResponseEntity<?> create(@RequestBody Person person) {
		try {
			personRepository.save(person);
			return new ResponseEntity<>(person, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/{name}")
	public ResponseEntity<?> delete(@PathVariable String name) {
		try {
			Person p = personRepository.findByName(name);
			if (p == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			personRepository.delete(p);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
