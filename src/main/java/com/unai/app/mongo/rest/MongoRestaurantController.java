package com.unai.app.mongo.rest;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;
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
import org.springframework.web.servlet.view.RedirectView;

import com.mongodb.client.MongoCursor;
import com.unai.app.mongo.MongoSession;
import com.unai.app.mongo.model.Restaurant;
import com.unai.app.utils.HTTPHeaders;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/mongo/restaurants")
@Api
public class MongoRestaurantController {
	
	private Logger log = Logger.getLogger(MongoRestaurantController.class);
	
	@GetMapping("/")
	@ApiOperation(notes="public RedirectView getFirst()::40", value="Gets the first hundred restaurants.")
	public RedirectView getFirst() {
		return new RedirectView("/mongo/restaurants/index/0");
	}
	
	@GetMapping("/id/{id}")
	@ApiOperation(notes="public ResponseEntity<?> getOne(String)::46", value="Gets the restaurant with the given ID.")
	public ResponseEntity<?> getOne(@PathVariable String id) {
		MongoSession session = null;
		try {
			session = MongoSession.create();
			Restaurant r = Restaurant.fromDocument(session.getCollection().find(eq("_id", new ObjectId(id))).first());
			return ResponseEntity.ok(r);
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
	
	@GetMapping("/index/{index}")
	@ApiOperation(notes="public ResponseEntity<?> getFrom(Integer)::65", value="Gets restaurants in lists of hundreds, depending on the received index number.")
	public ResponseEntity<?> getFrom(@PathVariable Integer index) {
		MongoSession session = null;
		try {
			session = MongoSession.create();
			List<Restaurant> list = new ArrayList<>();
			MongoCursor<Document> it = session.getCollection().find().skip(index).limit(100).iterator();
			while (it.hasNext()) list.add(Restaurant.fromDocument(it.next()));
			if (index + 100 < session.getCollection().count()) {
				HTTPHeaders headers = new HTTPHeaders().location(String.format("/mongo/index/%d", index + 100));
				return new ResponseEntity<>(list, headers, HttpStatus.OK);
			} else {
				return ResponseEntity.ok(list);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) session.close();
		}
	}
	
	@GetMapping("/id/{id}/address")
	@ApiOperation(notes="public ResponseEntity<?> getAddress(String)::88", value="Gets the address field of the restaurant with the given ID.")
	public ResponseEntity<?> getAddress(@PathVariable String id) {
		MongoSession session = null;
		try {
			session = MongoSession.create();
			Restaurant r = Restaurant.fromDocument(session.getCollection().find(eq("_id", new ObjectId(id))).first());
			return ResponseEntity.ok(r.getAddress());
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
	
	@PostMapping(value="/", consumes={"application/json"})
	@ApiOperation(notes="public ResponseEntity<?> create(Restaurant)::107", value="Creates a restaurant from the fields on the request body.")
	public ResponseEntity<?> create(@RequestBody Restaurant restaurant) {
		MongoSession session = null;
		try {
			session = MongoSession.create();
			Document d = restaurant.toDocument();
			session.getCollection().insertOne(d);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) session.close();
		}
	}
	
	@PutMapping(value="/id/{id}", consumes={"application/json"})
	@ApiOperation(notes="public ResponseEntity<?> update(String, Restaurant)::124", value="Updates the fields of the restaurant with the given ID from the fields of the request body.")
	public ResponseEntity<?> update(@PathVariable String id, @RequestBody Restaurant restaurant) {
		MongoSession session = null;
		try {
			session = MongoSession.create();
			session.getCollection().updateOne(eq("_id", new ObjectId(id)), restaurant.updater());
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) session.close();
		}
	}
	
	@DeleteMapping("/id/{id}")
	@ApiOperation(notes="public ResponseEntity<?> detele(String)::140", value="Deletes the restaurant with the given ID.")
	public ResponseEntity<?> delete(@PathVariable String id) {
		MongoSession session = null;
		try {
			session = MongoSession.create();
			session.getCollection().deleteOne(eq("_id", new ObjectId(id)));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (session != null) session.close();
		}
	}
	
}
