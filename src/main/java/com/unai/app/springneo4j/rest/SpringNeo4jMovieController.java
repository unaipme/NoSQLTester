package com.unai.app.springneo4j.rest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.unai.app.springneo4j.model.Movie;
import com.unai.app.springneo4j.repo.MovieRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api
@RequestMapping("/sneo4j/movies")
public class SpringNeo4jMovieController {

	private Logger log = Logger.getLogger(SpringNeo4jMovieController.class);
	
	@Autowired
	private MovieRepository movieRepository;
	
	@GetMapping("/")
	@ApiOperation("Gets all movies and their casts in the database.")
	@ApiResponses({
		@ApiResponse(code=200, message="All worked as expected (the set will be empty if no records were created)."),
		@ApiResponse(code=500, message="An unexpected error occurred. We will work on it.")
	})
	public ResponseEntity<?> getAll() {
		try {
			return ResponseEntity.ok(movieRepository.findAll());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{title}")
	@ApiOperation("Gets the information and cast of the movie with the given title.")
	@ApiResponses({
		@ApiResponse(code=200, message="All worked as expected."),
		@ApiResponse(code=404, message="No movie was found with the given title."),
		@ApiResponse(code=500, message="An unexpected error occurred. We will work on it.")
	})
	public ResponseEntity<?> getOne(@PathVariable String title) {
		try {
			Movie m = movieRepository.findByTitle(title);
			if (m == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			return ResponseEntity.ok(m);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value="/", consumes={"application/json"})
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation("Creates a new movie from the JSON body received.")
	@ApiResponses({
		@ApiResponse(code=201, message="The movie was created successfully."),
		@ApiResponse(code=500, message="An unexpected error occurred. We will work on it.")
	})
	public ResponseEntity<?> create(@RequestBody Movie movie) {
		try {
			movieRepository.save(movie);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/{title}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation("Deletes the movie with the given title.")
	@ApiResponses({
		@ApiResponse(code=204, message="The movie was successfully deleted (or didn't exist at all)."),
		@ApiResponse(code=500, message="An unexpected error occurred. We will work on it.")
	})
	public ResponseEntity<?> delete(@PathVariable String title) {
		try {
			Movie m = movieRepository.findByTitle(title);
			if (m == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			movieRepository.delete(m);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
}
