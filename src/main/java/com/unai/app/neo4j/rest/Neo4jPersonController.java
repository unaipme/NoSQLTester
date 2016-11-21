package com.unai.app.neo4j.rest;

import static com.unai.app.neo4j.Neo4jSession.getSession;

import org.neo4j.driver.v1.StatementResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unai.app.neo4j.Neo4jSession;
import com.unai.app.neo4j.service.PersonService;

@RestController
@RequestMapping("/neo4j")
public class Neo4jPersonController {
	
	@Value("${neo4j.server.uri}")
	private String uri;
	
	@Value("${neo4j.user.username}")
	private String username;
	
	@Value("${neo4j.user.password}")
	private String password;
	
	private Logger log = LoggerFactory.getLogger(Neo4jPersonController.class);
	
	@Autowired
	private PersonService personService;
	
	@GetMapping("/people")
	public ResponseEntity<?> getall() {
		Neo4jSession session = null;
		try {
			session = getSession(uri, username, password);
			StatementResult result = session.run("MATCH (person:Person)-[relation]->(movie:Movie) RETURN person, relation, movie");
			return ResponseEntity.ok(personService.getAll(result));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	@GetMapping("/people/{name}")
	public ResponseEntity<?> getOne(@PathVariable String name) {
		Neo4jSession session = null;
		try {
			session = getSession(uri, username, password);
			StatementResult result = session.run(String.format("MATCH (person:Person {name: '%s'})-[relation]->(movie:Movie) RETURN person, relation, movie", name));
			return ResponseEntity.ok(personService.getOne(result));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
}
