package com.unai.app.springredis.rest;

import java.util.HashMap;

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

import com.unai.app.redis.exception.SetEmptyException;
import com.unai.app.redis.model.SetResponse;
import com.unai.app.springredis.service.SetRepository;
import com.unai.app.utils.HTTPHeaders;



@RestController
@RequestMapping("/sredis")
public class SpringRedisSetController {
	
	private Logger log = LoggerFactory.getLogger(SpringRedisSetController.class);
	
	@Autowired
	private SetRepository setRepository;
	
	@PostMapping("/sadd/{key}/{members}")
	public ResponseEntity<?> sadd(@PathVariable("key") String key, @PathVariable("members") String [] members) {
		try {
			Long created = setRepository.sadd(key, (Object []) members);
			HashMap<String, Long> response = new HashMap<>();
			response.put("created", created);
			HttpHeaders h = new HTTPHeaders().location(String.format("/smembers/%s", key));
			return new ResponseEntity<HashMap<String, Long>>(response, h, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/smembers/{key}")
	public ResponseEntity<?> smembers(@PathVariable String key) {
		try {
			SetResponse st = new SetResponse(key, setRepository.smembers(key));
			return ResponseEntity.ok(st);
		} catch (SetEmptyException e) {
			log.error(e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/srem/{key}")
	public ResponseEntity<?> sremall(@PathVariable String key) {
		try {
			setRepository.sremall(key);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/srem/{key}/{members}")
	public ResponseEntity<?> srem(@PathVariable("key") String key, @PathVariable("field") String [] members) {
		try {
			Long count = setRepository.srem(key, (Object []) members);
			HashMap<String, Long> response = new HashMap<>();
			response.put("removed", count);
			if (count != members.length) {
				return new ResponseEntity<HashMap<String, Long>>(response, HttpStatus.ACCEPTED);
			} else {
				return new ResponseEntity<HashMap<String, Long>>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
