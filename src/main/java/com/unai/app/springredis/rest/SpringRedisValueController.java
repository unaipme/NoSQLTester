package com.unai.app.springredis.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unai.app.redis.exception.KeyValueNotFoundException;
import com.unai.app.redis.model.KeyValueResponse;
import com.unai.app.springredis.service.ValueRepository;
import com.unai.app.utils.HTTPHeaders;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/sredis")
@Api
public class SpringRedisValueController {
	
	@Autowired
	private ValueRepository valueRepository;
	
	private Logger log = LoggerFactory.getLogger(SpringRedisValueController.class);
	
	@GetMapping("/get/{key}")
	@ApiOperation(notes="public ResponseEntity<?> get(String)::35", value="Gets the value of the given key.")
	public ResponseEntity<?> get(@PathVariable String key) {
		try {
			String value = valueRepository.get(key).toString();
			KeyValueResponse sr = new KeyValueResponse(key, String.valueOf(value));
			return ResponseEntity.ok(sr);
		} catch (NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (KeyValueNotFoundException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/set/{key}/{value}")
	@ApiOperation(notes="public ResponseEntity<?> set(String, String)::54", value="Sets (or creates) the given key to the given value.")
	public ResponseEntity<?> set(@PathVariable("key") String key, @PathVariable("value") String value) {
		try {
			KeyValueResponse rs = new KeyValueResponse(key, value);
			valueRepository.set(key, value);
			HTTPHeaders h = new HTTPHeaders().location(String.format("/sredis/get/%s", key));
			return new ResponseEntity<KeyValueResponse>(rs, h, HttpStatus.OK);
		} catch (KeyValueNotFoundException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/del/{key}")
	@ApiOperation(notes="public ResponseEntity<?> del(String)::71", value="Deletes the given keys and their values.")
	public ResponseEntity<?> del(@PathVariable String key) {
		try {
			valueRepository.del(key);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
