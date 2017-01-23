package com.unai.app.springredis.rest;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unai.app.redis.exception.KeyValueNotFoundException;
import com.unai.app.redis.exception.ListEmptyException;
import com.unai.app.redis.model.KeyValueResponse;
import com.unai.app.redis.model.ListResponse;
import com.unai.app.springredis.service.ListRepository;
import com.unai.app.utils.HTTPHeaders;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/sredis")
public class SpringRedisListController {
	
	@Autowired
	private ListRepository listRepository;
	
	private Logger log = LoggerFactory.getLogger(SpringRedisListController.class);
	
	@PostMapping("/lpush/{key}/{values}")
	@ApiOperation(notes="public ResponseEntity<?> lpush(String, String [])::39", value="Adds (prepends) the values into the list with the given key.")
	public ResponseEntity<?> lpush(@PathVariable("key") String key, @PathVariable("values") String [] values) {
		try {
			Long created = listRepository.lpush(key, values);
			HashMap<String, Long> response = new HashMap<>();
			response.put("created", created);
			HttpHeaders h = new HTTPHeaders().location(String.format("/sredis/lindex/%s/all", key));
			return new ResponseEntity<HashMap<String, Long>>(response, h, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/lindex/{key}/{index}")
	@ApiOperation(notes="public ResponseEntity<?> lindex(String, Long)::54", value="Gets the element on the given index of the list with the given key.")
	public ResponseEntity<?> lindex(@PathVariable("key") String key, @PathVariable("index") Long index) {
		try {
			String result = listRepository.lindex(key, index).toString();
			KeyValueResponse sr = new KeyValueResponse(String.format("%s[%d]", key, index), result);
			return ResponseEntity.ok(sr);
		} catch (NullPointerException|KeyValueNotFoundException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/lindex/{key}/all")
	@ApiOperation(notes="public ResponseEntity<?> lall(String)::70", value="Gets all elements in list with given key.")
	public ResponseEntity<?> lall(@PathVariable String key) {
		try {
			List<Object> list = listRepository.lall(key);
			ListResponse lr = new ListResponse(key, list);
			return ResponseEntity.ok(lr);
		} catch (ListEmptyException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/lrem/{key}/all")
	@ApiOperation(notes="public ResponseEntity<?> lremall(String)::86", value="Removes the list with the given key and all its elements.")
	public ResponseEntity<?> lremall(@PathVariable String key) {
		try {
			listRepository.lrem(key);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/lrem/{key}/field/{field}")
	@ApiOperation(notes="public ResponseEntity<?> lrem(String, String)::98", value="Deletes all fields with given name from the list with given key.")
	public ResponseEntity<?> lrem(@PathVariable("key") String key, @PathVariable("field") String field) {
		try {
			listRepository.lrem(key, field);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/lrem/{key}/field/{field}/{count}")
	@ApiOperation(notes="public ResponseEntity<?> lrem(String, String, Long)::110", value="Deletes as many elements with the given name as specified by count, from the list with given key.")
	public ResponseEntity<?> lrem(@PathVariable("key") String key, @PathVariable("field") String field, @PathVariable("count") Long count) {
		try {
			listRepository.lrem(key, field, count);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
