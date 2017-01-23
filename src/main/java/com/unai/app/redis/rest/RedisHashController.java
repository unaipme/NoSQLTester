package com.unai.app.redis.rest;

import java.util.Map;

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
import com.unai.app.redis.exception.HashSetEmptyException;
import com.unai.app.redis.exception.KeyValueNotFoundException;
import com.unai.app.redis.model.HashResponse;
import com.unai.app.redis.model.KeyValueResponse;
import com.unai.app.utils.HTTPHeaders;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import redis.clients.jedis.exceptions.JedisDataException;

@RestController
@RequestMapping("/redis")
@Api
public class RedisHashController {
	
	private Logger log = LoggerFactory.getLogger(RedisHashController.class);
	
	@PostMapping(value="/hmset/{key}", consumes={"application/json"})
	@ApiOperation(notes="public ResponseEntity<?> hmset(String, Map<String, String>)::38", value="Adds the elements on the request body to the hash.")
	public ResponseEntity<?> hmset(@PathVariable String key, @RequestBody Map<String, String> hashset) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
			HashResponse hr = new HashResponse(key, hashset);
			jedis.hmset(hr);
			jedis.close();
			HttpHeaders headers = new HTTPHeaders().location(String.format("/redis/hmget/%s", hr.getKey()));
			return new ResponseEntity<HashResponse>(hr, headers, HttpStatus.CREATED);
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
	
	@PostMapping("/hset/{key}/{field}/{value}")
	@ApiOperation(notes="public ResponseEntity<?> hset(String, String, String)::62", value="Sets the value of an element with the given field name on the set with the given key.")
	public ResponseEntity<?> hset(@PathVariable("key") String key, @PathVariable("field") String field, @PathVariable String value) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
			KeyValueResponse sr = new KeyValueResponse(field, value);
			jedis.hset(key, sr);
			jedis.close();
			HttpHeaders headers = new HTTPHeaders().location(String.format("/redis/hget/%s/%s", key, sr.getKey()));
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
	
	@GetMapping("/hgetall/{key}")
	@ApiOperation(notes="public ResponseEntity<?> hgetall(String)::86", value="Gets all values of the hash with the given key.")
	public ResponseEntity<?> hgetall(@PathVariable String key) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
			HashResponse hr = new HashResponse(key, jedis.hgetAll(key));
			return ResponseEntity.ok(hr);
		} catch (HashSetEmptyException e) {
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
	
	@GetMapping("/hget/{key}/{field}")
	@ApiOperation(notes="public ResponseEntity<?> hget(String, String)::110", value="Gets only the specified fields from the set with the given key.")
	public ResponseEntity<?> hget(@PathVariable("key") String key, @PathVariable("field") String field) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
			KeyValueResponse sr = new KeyValueResponse(key, jedis.hget(key, field));
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
	
	@DeleteMapping("/hdel/{key}/{field}")
	@ApiOperation(notes="public ResponseEntity<?> hdel(String, String)::134", value="Deletes the specified fields from the set with the given key.")
	public ResponseEntity<?> hdel(@PathVariable("key") String key, @PathVariable("field") String field) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
			Long index = jedis.hdel(key, field);
			if (index == 0) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<>(HttpStatus.OK);
			}
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
	
	@DeleteMapping("/hdel/{key}")
	@ApiOperation(notes="public ResponseEntity<?> hdelall(String)", value="Deletes the set with the given key and all its elements.")
	public ResponseEntity<?> hdelall(@PathVariable String key) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
			Long index = jedis.del(key);
			if (index == 0) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<>(HttpStatus.OK);
			}
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
	
}
