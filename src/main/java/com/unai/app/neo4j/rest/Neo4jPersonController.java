package com.unai.app.neo4j.rest;

import static com.unai.app.neo4j.Neo4jSession.getSession;

import org.neo4j.driver.v1.StatementResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@RestController
@RequestMapping("/neo4j/people")
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
	
	@GetMapping("/")
	public ResponseEntity<?> getall() {
		Neo4jSession session = null;
		try {
			session = getSession(uri, username, password);
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
	public ResponseEntity<?> getOne(@PathVariable String name) {
		Neo4jSession session = null;
		try {
			session = getSession(uri, username, password);
			Properties p = Properties.createWhereProperties();
			p.add("name", name);
			StatementResult result = session.run(personService.selectWhereQuery(p));
			return ResponseEntity.ok(personService.getOne(result));
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
	public ResponseEntity<?> create(@RequestBody Person person) {
		Neo4jSession session = null;
		try {
			session = getSession(uri, username, password);
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
	public ResponseEntity<?> delete(@PathVariable String name) {
		Neo4jSession session = null;
		try {
			session = getSession(uri, username, password);
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
	public ResponseEntity<?> update(@PathVariable("where") String where, @PathVariable("set") String set) {
		Neo4jSession session = null;
		try {
			session = getSession(uri, username, password);
			session.run(personService.updateQuery(where, set));
			return new ResponseEntity<>(HttpStatus.OK);
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
