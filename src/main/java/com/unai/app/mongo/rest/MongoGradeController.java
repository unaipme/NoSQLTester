package com.unai.app.mongo.rest;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.push;
import static com.mongodb.client.model.Updates.pull;
import static com.mongodb.client.model.Updates.set;

import java.util.Date;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
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

import com.unai.app.mongo.MongoSession;
import com.unai.app.mongo.model.Grade;
import com.unai.app.mongo.model.Restaurant;

@RestController
@RequestMapping("/mongo/restaurants")
public class MongoGradeController {
	
	private Logger log = Logger.getLogger(MongoGradeController.class);
	
	@GetMapping("/id/{id}/grades")
	public ResponseEntity<?> getGrades(@PathVariable String id) {
		MongoSession session = null;
		try {
			session = MongoSession.create();
			Restaurant r = Restaurant.fromDocument(session.getCollection().find(eq("_id", new ObjectId(id))).first());
			return ResponseEntity.ok(r.getGrades());
		} catch (NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) session.close();
		}
	}
	
	@GetMapping("/id/{id}/grades/{index}")
	public ResponseEntity<?> getGrade(@PathVariable("id") String id, @PathVariable("index") Integer index) {
		MongoSession session = null;
		try {
			session = MongoSession.create();
			Restaurant r = Restaurant.fromDocument(session.getCollection().find(eq("_id", new ObjectId(id))).first());
			return ResponseEntity.ok(r.getGrades().get(index));
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) session.close();
		}
	}
	
	@PostMapping("/id/{id}/grades")
	public ResponseEntity<?> add(@PathVariable String id, @RequestBody Grade grade) {
		MongoSession session = null;
		try {
			session = MongoSession.create();
			if (grade.getDate() == null) {
				grade.setDate(new Date());
			}
			session.getCollection().updateOne(eq("_id", new ObjectId(id)), push("grades", grade.toDocument()));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) session.close();
		}
	}
	
	@PutMapping("/id/{id}/grades/{index}/score/{score}")
	public ResponseEntity<?> updateScore(@PathVariable("id") String id, @PathVariable("index") Integer index, @PathVariable("score") Integer score) {
		MongoSession session = null;
		try {
			session = MongoSession.create();
			Restaurant r = Restaurant.fromDocument(session.getCollection().find(eq("_id", new ObjectId(id))).first());
			Grade g = r.getGrades().get(index - 1);
			session.getCollection().updateOne(and(eq("_id", new ObjectId(id)), eq("grades.grade", g.getGrade()), eq("grades.score", g.getScore()), eq("grades.date", g.getDate())),
					set("grades.$.score", score));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) session.close();
		}
	}
	
	@PutMapping("/id/{id}/grades/{index}/grade/{grade}")
	public ResponseEntity<?> updateGrade(@PathVariable("id") String id, @PathVariable("index") Integer index, @PathVariable("grade") String grade) {
		MongoSession session = null;
		try {
			session = MongoSession.create();
			Restaurant r = Restaurant.fromDocument(session.getCollection().find(eq("_id", new ObjectId(id))).first());
			Grade g = r.getGrades().get(index - 1);
			session.getCollection().updateOne(and(eq("_id", new ObjectId(id)), eq("grades.grade", g.getGrade()), eq("grades.score", g.getScore()), eq("grades.date", g.getDate())),
					set("grades.$.grade", grade));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) session.close();
		}
	}
	
	@DeleteMapping("/id/{id}/grades/{index}")
	public ResponseEntity<?> delete(@PathVariable("id") String id, @PathVariable("index") Integer index) {
		MongoSession session = null;
		try {
			session = MongoSession.create();
			Restaurant r = Restaurant.fromDocument(session.getCollection().find(eq("_id", new ObjectId(id))).first());
			Grade g = r.getGrades().get(index - 1);
			session.getCollection().updateOne(eq("_id", new ObjectId(id)),
					pull("grades", combine(eq("grade", g.getGrade()), eq("score", g.getScore()), eq("date", g.getDate()))));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) session.close();
		}
	}
}
