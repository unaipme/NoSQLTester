package com.unai.app.redis.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.unai.app.redis.JedisDriver;
import com.unai.app.redis.exception.KeyValueNotFoundException;
import com.unai.app.redis.model.KeyValueResponse;
import com.unai.app.utils.HTTPHeaders;

import io.swagger.annotations.ApiOperation;
import redis.clients.jedis.exceptions.JedisDataException;

@RestController
@RequestMapping("/redis")
public class RedisValueController {
	
	private Logger log = LoggerFactory.getLogger(RedisValueController.class);
	
	@GetMapping("/get/{key}")
	@ApiOperation(notes="public ResponseEntity<?> get(String)::32", value="Gets the value of the given key.")
	public ResponseEntity<?> get(@PathVariable String key) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
			KeyValueResponse sr = new KeyValueResponse(key, jedis.get(key));
			return ResponseEntity.ok(sr);
		} catch (KeyValueNotFoundException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (JedisDataException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	@PostMapping(value="/set/{key}/{value}")
	@ApiOperation(notes="public ResponseEntity<?> set(String, String)::56", value="Sets (or creates) the given key to the given value.")
	public ResponseEntity<?> set(@PathVariable("key") String key, @PathVariable("value") String value) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
			KeyValueResponse sr = new KeyValueResponse(key, value);
			jedis.set(sr);
			HttpHeaders headers = new HTTPHeaders().location(String.format("/redis/set/%s", key));
			return new ResponseEntity<KeyValueResponse>(sr, headers, HttpStatus.CREATED);
		} catch (JedisDataException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	@PostMapping("/set")
	@ApiOperation(notes="public ResponseEntity<?> set(KeyValueResponse)::80", value="Sets (or creates) the key to the value, from the request body.")
	public ResponseEntity<?> set(@RequestBody KeyValueResponse sr) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
			jedis.set(sr);
			HttpHeaders headers = new HTTPHeaders().location(String.format("/redis/set/%s", sr.getKey()));
			return new ResponseEntity<KeyValueResponse>(sr, headers, HttpStatus.CREATED);
		} catch (JedisDataException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	@DeleteMapping("/del/{key}")
	@ApiOperation(notes="public ResponseEntity<?> del(String [])::102", value="Deletes the given keys and their values.")
	public ResponseEntity<?> del(@PathVariable String [] key) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
			Long count = jedis.del(key);
			if (count == 0) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch(JedisDataException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
}
