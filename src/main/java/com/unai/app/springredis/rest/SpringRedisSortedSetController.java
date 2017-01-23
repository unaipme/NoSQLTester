package com.unai.app.springredis.rest;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unai.app.redis.exception.SetEmptyException;
import com.unai.app.redis.model.SetResponse;
import com.unai.app.springredis.service.SortedSetRepository;
import com.unai.app.utils.HTTPHeaders;

import io.swagger.annotations.ApiOperation;



@RestController
@RequestMapping("/sredis")
public class SpringRedisSortedSetController {
	
	@Autowired
	private SortedSetRepository zsetRepository;
	
	private Logger log = LoggerFactory.getLogger(SpringRedisSortedSetController.class);
	
	@GetMapping("/zrange/{key}/all")
	@ApiOperation(notes="public ResponseEntity<?> zrangeall(String)::40", value="Gets all elements in the sorted list with the given key.")
	public ResponseEntity<?> zrangeall(@PathVariable String key) {
		try {
			SetResponse sr = new SetResponse(key, zsetRepository.zrange(key));
			return ResponseEntity.ok(sr);
		} catch (SetEmptyException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/zrange/{key}/{start}/{stop}")
	@ApiOperation(notes="public ResponseEntity<?>(String, Long, Long)::55", value="Gets the range specified of the sorted list with the given key.")
	public ResponseEntity<?> zrange(@PathVariable("key") String key, @PathVariable("start") Long start, @PathVariable("stop") Long stop) {
		try {
			SetResponse sr = new SetResponse(key, zsetRepository.zrange(key, start, stop));
			return ResponseEntity.ok(sr);
		} catch (SetEmptyException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/zadd/{key}/{score}/{value}")
	@ApiOperation(notes="public ResponseEntity<?> zadd(String, Double, String)::70", value="Adds a value with the given score to the sorted list with the given key.")
	public ResponseEntity<?> zadd(@PathVariable("key") String key, @PathVariable("score") Double score, @PathVariable("value") String value) {
		try {
			Map<String, Long> response = new HashMap<>();
			response.put("created", zsetRepository.zadd(key, value, score));
			HttpHeaders h = new HTTPHeaders().location(String.format("/sredis/zrange/%s/all", key));
			return new ResponseEntity<Map<String, Long>>(response, h, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value="/zadd/{key}", consumes={"application/json"})
	@ApiOperation(notes="public ResponseEntity<?> zadd(String, Map<String, Double>)::84", value="Adds the map of values with the corresponding score to the sorted list with given key.")
	public ResponseEntity<?> zadd(@PathVariable String key, @RequestBody Map<String, Double> map) {
		try {
			Map<String, Long> response = new HashMap<>();
			response.put("created", zsetRepository.zadd(key, map));
			HttpHeaders h = new HTTPHeaders().location(String.format("/sredis/zrange/%s/all", key));
			return new ResponseEntity<Map<String, Long>>(response, h, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/zrem/{key}/{field}")
	@ApiOperation(notes="public ResponseEntity<?> zrem(String, String [])::98", value="Deletes from the sorted list with the given key the specified values.")
	public ResponseEntity<?> zrem(@PathVariable("key") String key, @PathVariable("field") String [] fields) {
		try {
			Map<String, Long> response = new HashMap<>();
			response.put("deleted", zsetRepository.zrem(key, (Object []) fields));
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
