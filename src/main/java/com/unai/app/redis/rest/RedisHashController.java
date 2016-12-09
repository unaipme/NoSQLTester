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

import redis.clients.jedis.exceptions.JedisDataException;

@RestController
@RequestMapping("/redis")
public class RedisHashController {
	
	private Logger log = LoggerFactory.getLogger(RedisHashController.class);
	
	@PostMapping(value="/hmset/{key}", consumes={"application/json"})
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
