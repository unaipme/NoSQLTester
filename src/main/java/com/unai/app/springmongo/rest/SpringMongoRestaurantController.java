package com.unai.app.springmongo.rest;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

import com.unai.app.springmongo.model.Restaurant;
import com.unai.app.springmongo.repo.RestaurantRepository;
import com.unai.app.utils.HTTPHeaders;

@RestController
@RequestMapping("/smongo/restaurants")
public class SpringMongoRestaurantController {
	
	private Logger log = Logger.getLogger(SpringMongoRestaurantController.class);
	
	@Autowired
	private RestaurantRepository repo;
	
	@GetMapping("/")
	public RedirectView getFirst() {
		return new RedirectView("/smongo/restaurants/page/0");
	}
	
	@GetMapping("/page/{page}")
	public ResponseEntity<?> getFrom(@PathVariable Integer page) {
		try {
			HTTPHeaders h = new HTTPHeaders().location(String.format("/smongo/from/%d", page + 1));
			return new ResponseEntity<>(repo.findAll(new PageRequest(page, 100)), h, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<?> getOne(@PathVariable String id) {
		try {
			Restaurant r = repo.findOne(id);
			if (r == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				return ResponseEntity.ok(r);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/id/{id}/address")
	public ResponseEntity<?> getAddress(@PathVariable String id) {
		try {
			Restaurant r = repo.findOne(id);
			if (r == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				return ResponseEntity.ok(r.getAddress());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value="/", consumes={"application/json"})
	public ResponseEntity<?> create(@RequestBody Restaurant restaurant) {
		try {
			repo.insert(restaurant);
			HTTPHeaders h = new HTTPHeaders().location(String.format("/smongo/restaurants/id/%s", restaurant.getId()));
			return new ResponseEntity<>(restaurant, h, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(value="/id/{id}", consumes={"application/json"})
	public ResponseEntity<?> update(@PathVariable String id, @RequestBody Map<String, Object> properties) {
		try {
			Restaurant restaurant = repo.findOne(id);
			for (Map.Entry<String, Object> entry : properties.entrySet()) {
				restaurant.set(entry.getKey(), entry.getValue());
			}
			repo.save(restaurant);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/id/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {
		try {
			repo.delete(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
