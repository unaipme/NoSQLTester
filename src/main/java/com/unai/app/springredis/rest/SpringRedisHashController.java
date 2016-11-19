package com.unai.app.springredis.rest;

import java.net.URI;
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
import com.unai.app.redis.model.StringResponse;
import com.unai.app.springredis.service.HashRepository;

@RestController
@RequestMapping("/sredis")
public class SpringRedisHashController {
	
	@Autowired
	private HashRepository hashRepository;
	
	private Logger log = LoggerFactory.getLogger(SpringRedisHashController.class);
	
	@PostMapping(value="/hmset/{key}", consumes={"application/json"})
	public ResponseEntity<?> hmset(@PathVariable String key, @RequestBody Map<Object, Object> hashset) {
		try {
			hashRepository.hmset(key, hashset);
			HttpHeaders h = new HttpHeaders();
			h.setLocation(URI.create(String.format("/sredis/hgetall/%s", key)));
			return new ResponseEntity<HashResponse>(new HashResponse(key, hashset), h, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/hset/{key}/{field}/{value}")
	public ResponseEntity<?> hset(@PathVariable("key") String key, @PathVariable("field") String field, @PathVariable("value") String value) {
		try {
			hashRepository.hset(key, field, value);
			HttpHeaders h = new HttpHeaders();
			h.setLocation(URI.create(String.format("/sredis/hget/%s/%s", key, field)));
			HashResponse hr = new HashResponse(key, hashRepository.hgetall(key));
			return new ResponseEntity<HashResponse>(hr, h, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/hgetall/{key}")
	public ResponseEntity<?> hgetall(@PathVariable String key) {
		try {
			Map<Object, Object> map = hashRepository.hgetall(key);
			HashResponse hr = new HashResponse(key, map);
			return new ResponseEntity<HashResponse>(hr, HttpStatus.OK);
		} catch (HashSetEmptyException e) {
			log.error(e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/hget/{key}/{field}")
	public ResponseEntity<?> hget(@PathVariable("key") String key, @PathVariable("field") String field) {
		try {
			StringResponse sr = new StringResponse(String.format("%s[%s]", key, field), hashRepository.hget(key, field).toString());
			return new ResponseEntity<StringResponse>(sr, HttpStatus.OK);
		} catch (NullPointerException|KeyValueNotFoundException e) {
			log.error(e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/hdel/{key}/{field}")
	public ResponseEntity<?> hdel(@PathVariable("key") String key, @PathVariable("field") String field) {
		try {
			hashRepository.hdel(key, field);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
