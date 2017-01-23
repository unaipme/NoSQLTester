package com.unai.app.springmongo.rest;

import java.util.Date;

import org.apache.log4j.Logger;
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

import com.unai.app.springmongo.model.Grade;
import com.unai.app.springmongo.model.Restaurant;
import com.unai.app.springmongo.repo.RestaurantRepository;
import com.unai.app.utils.HTTPHeaders;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/smongo/restaurants")
@Api
public class SpringMongoGradeController {
	
	private Logger log = Logger.getLogger(SpringMongoGradeController.class);
	
	@Autowired
	private RestaurantRepository repo;
	
	@GetMapping("/id/{id}/grades")
	@ApiOperation(notes="public ResponseEntity<?> getGrades(String)::38", value="Gets the grades of the restaurant with the given ID.")
	public ResponseEntity<?> getGrades(@PathVariable String id) {
		try {
			Restaurant r = repo.findOne(id);
			return ResponseEntity.ok(r.getGrades());
		} catch (NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/id/{id}/grades/{index}")
	@ApiOperation(notes="public ResponseEntity<?> getGrade(String, Integer)::53", value="Get the grade in the given index of the restaurant with the given ID.")
	public ResponseEntity<?> getGrade(@PathVariable("id") String id, @PathVariable("index") Integer index) {
		try {
			Restaurant r = repo.findOne(id);
			Grade g = r.getGrades().get(index - 1);
			return new ResponseEntity<>(g, HttpStatus.OK);
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/id/{id}/grades")
	@ApiOperation(notes="public ResponseEntity<?> add(String, Grade)::69", value="Adds to the restaurant with the given ID a grade with the fields of the request body.")
	public ResponseEntity<?> add(@PathVariable String id, @RequestBody Grade grade) {
		try {
			Restaurant r = repo.findOne(id);
			int index = r.getGrades().size() + 1;
			if (grade.getDate() == null) {
				grade.setDate(new Date());
			}
			r.getGrades().add(grade);
			repo.save(r);
			HTTPHeaders h = new HTTPHeaders().location(String.format("/smongo/restaurants/id/%s/grades/%d", id, index));
			return new ResponseEntity<>(h, HttpStatus.CREATED);
		} catch (NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/id/{id}/grades/{index}/score/{score}")
	@ApiOperation(notes="public ResponseEntity<?> updateScore(String, Integer, Integer)::91", value="Updates the score of the grade of the specified index of the restaurant with the given ID.")
	public ResponseEntity<?> updateScore(@PathVariable("id") String id, @PathVariable("index") Integer index, @PathVariable("score") Integer score) {
		try {
			Restaurant r = repo.findOne(id);
			Grade g = r.getGrades().get(index - 1);
			g.setScore(score);
			repo.save(r);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/id/{id}/grades/{index}/grade/{grade}")
	@ApiOperation(notes="public ResponseEntity<?> updateGrade(String, Integer, String)::109", value="Updates the grade of the specified index of the restaurant with the given ID.")
	public ResponseEntity<?> updateGrade(@PathVariable("id") String id, @PathVariable("index") Integer index, @PathVariable("grade") String grade) {
		try {
			Restaurant r = repo.findOne(id);
			Grade g = r.getGrades().get(index - 1);
			g.setGrade(grade);
			repo.save(r);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/id/{id}/grades/{index}")
	@ApiOperation(notes="public ResponseEntity<?> delete(String, Integer)::127", value="Deletes the grade of the given index from the restaurant with the given ID.")
	public ResponseEntity<?> delete(@PathVariable("id") String id, @PathVariable("index") Integer index) {
		try {
			Restaurant r = repo.findOne(id);
			r.getGrades().remove(index - 1);
			repo.save(r);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
