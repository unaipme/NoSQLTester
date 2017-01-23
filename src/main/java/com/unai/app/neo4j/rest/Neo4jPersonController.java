package com.unai.app.neo4j.rest;

import org.neo4j.driver.v1.StatementResult;
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
import org.springframework.web.bind.annotation.RestController;

import com.unai.app.neo4j.Neo4jSession;
import com.unai.app.neo4j.model.Person;
import com.unai.app.neo4j.model.Properties;
import com.unai.app.neo4j.service.PersonService;
import com.unai.app.utils.HTTPHeaders;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api
@RequestMapping("/neo4j/people")
public class Neo4jPersonController {
	
	private Logger log = LoggerFactory.getLogger(Neo4jPersonController.class);
	
	@Autowired
	private PersonService personService;
	
	@GetMapping("/")
	@ApiOperation(notes="public ResponseEntity<?> getAll()::45", value="Gets the information of all the people stored in the database.")
	@ApiResponses({
		@ApiResponse(code=200, message="All went as expected."),
		@ApiResponse(code=500, message="An unexpected error occurred. We will work on it.")
	})
	public ResponseEntity<?> getAll() {
		Neo4jSession session = null;
		try {
			session = new Neo4jSession();
			StatementResult result = session.run(personService.selectWhereQuery(Properties.createWhereProperties()));
			return ResponseEntity.ok(personService.getAll(result));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	@GetMapping("/{name}")
	@ApiOperation(notes="public ResponseEntity<?> getOne(String)::68", value="Gets the information of the person with the given name.")
	@ApiResponses({
		@ApiResponse(code=200, message="All worked as expected."),
		@ApiResponse(code=404, message="No person was found with the given name."),
		@ApiResponse(code=500, message="An unexpected error occurred. We will work on it.")
	})
	public ResponseEntity<?> getOne(@PathVariable String name) {
		Neo4jSession session = null;
		try {
			session = new Neo4jSession();
			Properties p = Properties.createWhereProperties();
			p.add("name", name);
			StatementResult result = session.run(personService.selectWhereQuery(p));
			Person person = personService.getOne(result);
			if (person == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				return ResponseEntity.ok(person);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	@PostMapping(value="/", consumes={"application/json"})
	@ApiOperation(notes="public ResponseEntity<?> create(Person)::92", value="Creates a new person on the database from the JSON body received.")
	@ApiResponses({
		@ApiResponse(code=201, message="The person was successfully created."),
		@ApiResponse(code=500, message="An unexpected error occurred. We will work on it.")
	})
	public ResponseEntity<?> create(@RequestBody Person person) {
		Neo4jSession session = null;
		try {
			session = new Neo4jSession();
			session.run(personService.createQuery(person));
			HTTPHeaders h = new HTTPHeaders().location(String.format("/neo4j/people/%s", person.getName().replaceAll(" ", "%20")));
			return new ResponseEntity<>(h, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	@DeleteMapping("/{name}")
	@ApiOperation(notes="public ResponseEntity<?> delete(String)::120", value="Deletes the person with the given name.")
	@ApiResponses({
		@ApiResponse(code=204, message="The person was successfully deleted (or didn't exist in the first place)."),
		@ApiResponse(code=500, message="An unexpected error occurred. We will work on it.")
	})
	public ResponseEntity<?> delete(@PathVariable String name) {
		Neo4jSession session = null;
		try {
			session = new Neo4jSession();
			Properties p = Properties.createWhereProperties();
			p.add("name", name);
			session.run(personService.deleteQuery(p));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	@PutMapping("/{where}/{set}")
	@ApiOperation(notes="public ResponseEntity<?> update(String, String)::144", value="Updates the people that match with the comma-separated clauses in the \"where\" part of the URL, setting the comma-separated properties.")
	@ApiResponses({
		@ApiResponse(code=204, message="The people were successfully updated."),
		@ApiResponse(code=500, message="An unexpected error occurred. We will work on it.")
	})
	public ResponseEntity<?> update(@PathVariable("where") String where, @PathVariable("set") String set) {
		Neo4jSession session = null;
		try {
			session = new Neo4jSession();
			session.run(personService.updateQuery(where, set));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
}
