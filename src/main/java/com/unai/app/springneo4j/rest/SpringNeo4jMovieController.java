package com.unai.app.springneo4j.rest;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.unai.app.springneo4j.model.Movie;
import com.unai.app.springneo4j.repo.MovieRepository;

@RestController
@RequestMapping("/sneo4j/movies")
public class SpringNeo4jMovieController {

	private Logger log = Logger.getLogger(SpringNeo4jMovieController.class);
	
	@Autowired
	private MovieRepository movieRepository;
	
	@GetMapping("/")
	public List<Movie> getAll() {
		return movieRepository.findAll();
	}
	
	@GetMapping("/{title}")
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
