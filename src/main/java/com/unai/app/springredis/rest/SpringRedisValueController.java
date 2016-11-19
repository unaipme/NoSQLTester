package com.unai.app.springredis.rest;

import java.net.URI;

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
import com.unai.app.redis.model.StringResponse;
import com.unai.app.springredis.service.ValueRepository;

@RestController
@RequestMapping("/sredis")
public class SpringRedisValueController {
	
	@Autowired
	private ValueRepository valueRepository;
	
	private Logger log = LoggerFactory.getLogger(SpringRedisValueController.class);
	
	@GetMapping("/get/{key}")
	public ResponseEntity<?> get(@PathVariable String key) {
		try {
			String value = valueRepository.get(key).toString();
			StringResponse sr = new StringResponse(key, String.valueOf(value));
			return ResponseEntity.ok(sr);
		} catch (NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} catch (KeyValueNotFoundException e) {
			log.error(e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/set/{key}/{value}")
	public ResponseEntity<?> set(@PathVariable("key") String key, @PathVariable("value") String value) {
		try {
			StringResponse rs = new StringResponse(key, value);
			valueRepository.set(key, value);
			HttpHeaders h = new HttpHeaders();
			h.setLocation(URI.create(String.format("/sredis/get/%s", key)));
			return new ResponseEntity<StringResponse>(rs, h, HttpStatus.OK);
		} catch (KeyValueNotFoundException e) {
			log.error(e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/del/{key}")
	public ResponseEntity<?> del(@PathVariable String key) {
		try {
			valueRepository.del(key);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}