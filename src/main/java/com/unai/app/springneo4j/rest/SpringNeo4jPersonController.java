package com.unai.app.springneo4j.rest;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.unai.app.springneo4j.model.Person;
import com.unai.app.springneo4j.service.SpringPersonService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api
@RequestMapping("/sneo4j/people")
public class SpringNeo4jPersonController {
	
	private Logger log = LoggerFactory.getLogger(SpringNeo4jPersonController.class);
	
	@Autowired
	private SpringPersonService personService;
	
	@GetMapping("/")
	@ApiOperation("Gets all the information of all people stored in the database.")
	@ApiResponse(code=200, message="All worked as expected")
	public List<Person> getAll() {
		return personService.findAll();
	}
	
	@GetMapping("/{name}")
	@ApiOperation("Gets all the information of the person with the given name.")
	@ApiResponses({
		@ApiResponse(code=200, message="All worked as expected."),
		@ApiResponse(code=404, message="No person with the given name was found in the database."),
		@ApiResponse(code=500, message="An unexpected error occurred. We will work on it.")
	})
	public ResponseEntity<?> getOne(@PathVariable String name) {
		try {
			Person p = personService.findByName(name);
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
	@ApiOperation("Gets all people that are older than the specified age.")
	@ApiResponse(code=200, message="All worked as expected (But result set may be empty because no one is older than that age).")
	public List<Person> getOlderThan(@PathVariable Integer age) {
		return personService.findOlderThan(age);
	}
	
	@PostMapping(value="/people", consumes={"application/json"})
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation("Creates the person in the database, from the JSON body received.")
	@ApiResponses({
		@ApiResponse(code=201, message="The person was created successfully."),
		@ApiResponse(code=500, message="An unexpected error occurred. We will work on it.")
	})
	public ResponseEntity<?> create(@RequestBody Person person) {
		try {
			personService.save(person);
			return new ResponseEntity<>(person, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/{name}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation("Deletes a person with the given name from the database.")
	@ApiResponses({
		@ApiResponse(code=204, message="The person was successfully deleted."),
		@ApiResponse(code=404, message="No person was found with the given name."),
		@ApiResponse(code=500, message="An unexpected error occurred. We will work on it.")
	})
	public ResponseEntity<?> delete(@PathVariable String name) {
		try {
			Person p = personService.findByName(name);
			if (p == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			personService.delete(p);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/{name}/acted_in/{title}/as/{role}")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation("Records an actor's appearance to a movie.")
	@ApiResponses({
		@ApiResponse(code=201, message="The new role was successfully created."),
		@ApiResponse(code=500, message="An unexpected error occurred. We will work on it.")
	})
	public ResponseEntity<?> actedIn(@PathVariable("name") String name, @PathVariable("title") String title, @PathVariable("role") String [] role) {
		try {
			personService.newRole(name, title, role);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
