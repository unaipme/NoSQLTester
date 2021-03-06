package com.unai.app.springredis.rest;

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

import com.unai.app.redis.exception.HashSetEmptyException;
import com.unai.app.redis.exception.KeyValueNotFoundException;
import com.unai.app.redis.model.HashResponse;
import com.unai.app.redis.model.KeyValueResponse;
import com.unai.app.springredis.service.HashRepository;
import com.unai.app.utils.HTTPHeaders;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/sredis")
public class SpringRedisHashController {
	
	@Autowired
	private HashRepository hashRepository;
	
	private Logger log = LoggerFactory.getLogger(SpringRedisHashController.class);
	
	@PostMapping(value="/hmset/{key}", consumes={"application/json"})
	@ApiOperation(notes="public ResponseEntity<?> hmset(String, Map<Object, Object>)::39", value="Adds the elements on the request body to the hash.")
	public ResponseEntity<?> hmset(@PathVariable String key, @RequestBody Map<Object, Object> hashset) {
		try {
			hashRepository.hmset(key, hashset);
			HttpHeaders h = new HTTPHeaders().location(String.format("/sredis/hgetall/%s", key));
			return new ResponseEntity<HashResponse>(new HashResponse(key, hashset), h, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/hset/{key}/{field}/{value}")
	@ApiOperation(notes="public ResponseEntity<?> hset(String, String, String)::52", value="Sets the value of an element with the given field name on the set with the given key.")
	public ResponseEntity<?> hset(@PathVariable("key") String key, @PathVariable("field") String field, @PathVariable("value") String value) {
		try {
			hashRepository.hset(key, field, value);
			HttpHeaders h = new HTTPHeaders().location(String.format("/sredis/hget/%s/%s", key, field));
			HashResponse hr = new HashResponse(key, hashRepository.hgetall(key));
			return new ResponseEntity<HashResponse>(hr, h, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/hgetall/{key}")
	@ApiOperation(notes="public ResponseEntity<?> hgetall(String)::66", value="Gets all values of the hash with the given key.")
	public ResponseEntity<?> hgetall(@PathVariable String key) {
		try {
			Map<Object, Object> map = hashRepository.hgetall(key);
			HashResponse hr = new HashResponse(key, map);
			return new ResponseEntity<HashResponse>(hr, HttpStatus.OK);
		} catch (HashSetEmptyException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/hget/{key}/{field}")
	@ApiOperation(notes="public ResponseEntity<?> hget(String, String)::82", value="Gets only the specified fields from the set with the given key.")
	public ResponseEntity<?> hget(@PathVariable("key") String key, @PathVariable("field") String field) {
		try {
			KeyValueResponse sr = new KeyValueResponse(String.format("%s[%s]", key, field), hashRepository.hget(key, field).toString());
			return new ResponseEntity<KeyValueResponse>(sr, HttpStatus.OK);
		} catch (NullPointerException|KeyValueNotFoundException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/hdel/{key}/{field}")
	@ApiOperation(notes="public ResponseEntity<?> hdel(String, String)::97", value="Deletes the specified fields from the set with the given key.")
	public ResponseEntity<?> hdel(@PathVariable("key") String key, @PathVariable("field") String field) {
		try {
			hashRepository.hdel(key, field);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
