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
import com.unai.app.neo4j.model.Movie;
import com.unai.app.neo4j.model.Properties;
import com.unai.app.neo4j.service.MovieService;
import com.unai.app.utils.HTTPHeaders;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/neo4j/movies")
@Api
public class Neo4jMovieController {
	
	private Logger log = LoggerFactory.getLogger(Neo4jMovieController.class);
	
	@Autowired
	private MovieService movieService;
	
	@GetMapping("/")
	@ApiOperation(notes="public ResponseEntity<?> getAll()::45", value="Gets all movies and their casts in the database.")
	@ApiResponses({
		@ApiResponse(code=200, message="All worked as expected (the set will be empty if no records were created)."),
		@ApiResponse(code=500, message="An unexpected error occurred. We will work on it.")
	})
	public ResponseEntity<?> getAll() {
		Neo4jSession session = null;
		try {
			session = new Neo4jSession();
			StatementResult result = session.run("MATCH (movie:Movie)<-[relation]-(person:Person) RETURN movie, relation, person");
			return ResponseEntity.ok(movieService.getAll(result));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	@GetMapping("/{title}")
	@ApiOperation(notes="public ResponseEntity<?> getOne(String)::68", value="Gets the information of the movie with the given title.")
	@ApiResponses({
		@ApiResponse(code=200, message="Everything went as expected."),
		@ApiResponse(code=404, message="No movie was found with the given title."),
		@ApiResponse(code=500, message="An unexpected error occurred. We will work on it.")
	})
	public ResponseEntity<?> getOne(@PathVariable String title) {
		Neo4jSession session = null;
		try {
			session = new Neo4jSession();
			Properties p = Properties.createWhereProperties();
			p.add("title", title);
			StatementResult result = session.run(movieService.selectWhereQuery(p));
			Movie m = movieService.getOne(result);
			if (m == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				return ResponseEntity.ok(m);
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
	@ApiOperation(notes="public ResponseEntity<?> create(Movie)::97", value="Creates a new movie from the JSON body received.")
	@ApiResponses({
		@ApiResponse(code=201, message="The movie was created successfully."),
		@ApiResponse(code=500, message="An unexpected error occurred. We will work on it.")
	})
	public ResponseEntity<?> create(@RequestBody Movie movie) {
		Neo4jSession session = null;
		try {
			session = new Neo4jSession();
			session.run(movieService.createQuery(movie));
			HTTPHeaders h = new HTTPHeaders().location(String.format("/neo4j/movies/%s", movie.getTitle().replaceAll(" ", "%20")));
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
	
	@DeleteMapping("/{title}")
	@ApiOperation(notes="public ResponseEntity<?> delete(String)::120", value="Deletes the movie with the given title.")
	@ApiResponses({
		@ApiResponse(code=204, message="The movie was successfully deleted (or didn't exist at all)."),
		@ApiResponse(code=500, message="An unexpected error occurred. We will work on it.")
	})
	public ResponseEntity<?> delete(@PathVariable String title) {
		Neo4jSession session = null;
		try {
			session = new Neo4jSession();
			Properties p = Properties.createWhereProperties();
			p.add("title", title);
			session.run(movieService.deleteQuery(p));
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
	@ApiOperation(notes="public ResponseEntity<?> update(String, String)::144", value="Updates the movies that match with the comma-separated clauses in the \"where\" part of the URL, setting the comma-separated properties.")
	@ApiResponses({
		@ApiResponse(code=200, message="The movies were successfully updated."),
		@ApiResponse(code=500, message="An unexpected error occurred. We will work on it.")
	})
	public ResponseEntity<?> update(@PathVariable("where") String where, @PathVariable("set") String set) {
		Neo4jSession session = null;
		try {
			session = new Neo4jSession();
			session.run(movieService.updateQuery(where, set));
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
