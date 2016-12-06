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
import com.unai.app.neo4j.model.Movie;
import com.unai.app.neo4j.model.Properties;
import com.unai.app.neo4j.service.MovieService;
import com.unai.app.utils.HTTPHeaders;

@RestController
@RequestMapping("/neo4j")
public class Neo4jMovieController {
	
	@Value("${neo4j.server.uri}")
	private String uri;
	
	@Value("${neo4j.user.username}")
	private String username;
	
	@Value("${neo4j.user.password}")
	private String password;
	
	private Logger log = LoggerFactory.getLogger(Neo4jMovieController.class);
	
	@Autowired
	private MovieService movieService;
	
	@GetMapping("/movies")
	public ResponseEntity<?> getAll() {
		Neo4jSession session = null;
		try {
			session = getSession(uri, username, password);
			StatementResult result = session.run("MATCH (movie:Movie)<-[relation]-(person:Person) RETURN movie, relation, person");
			return ResponseEntity.ok(movieService.getAll(result));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	@GetMapping("/movies/{title}")
	public ResponseEntity<?> getOne(@PathVariable String title) {
		Neo4jSession session = null;
		try {
			session = getSession(uri, username, password);
			Properties p = Properties.createWhereProperties();
			p.add("title", title);
			StatementResult result = session.run(movieService.selectWhereQuery(p));
			return ResponseEntity.ok(movieService.getOne(result));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	@PostMapping(value="/movies", consumes={"application/json"})
	public ResponseEntity<?> create(@RequestBody Movie movie) {
		Neo4jSession session = null;
		try {
			session = getSession(uri, username, password);
			session.run(movieService.createQuery(movie));
			HTTPHeaders h = new HTTPHeaders().location(String.format("/neo4j/movies/%s", movie.getTitle().replaceAll(" ", "%20")));
			return new ResponseEntity<Void>(h, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	@DeleteMapping("/movie/{title}")
	public ResponseEntity<?> delete(@PathVariable String title) {
		Neo4jSession session = null;
		try {
			session = getSession(uri, username, password);
			Properties p = Properties.createWhereProperties();
			p.add("title", title);
			session.run(movieService.deleteQuery(p));
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	@PutMapping("/movies/{where}/{set}")
	public ResponseEntity<?> update(@PathVariable("where") String where, @PathVariable("set") String set) {
		Neo4jSession session = null;
		try {
			session = getSession(uri, username, password);
			session.run(movieService.updateQuery(where, set));
			return new ResponseEntity<Void>(HttpStatus.OK);
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
