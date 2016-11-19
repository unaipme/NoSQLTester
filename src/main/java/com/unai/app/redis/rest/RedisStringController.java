package com.unai.app.redis.rest;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import com.unai.app.redis.model.StringResponse;

import redis.clients.jedis.exceptions.JedisDataException;

@RestController
@RequestMapping("/redis")
public class RedisStringController {
	
	@Value("${redis.server.ip}")
	private String redisIP;
	
	@Value("${redis.server.port}")
	private int port;
	
	private Logger log = LoggerFactory.getLogger(RedisStringController.class);
	
	@GetMapping("/get/{key}")
	public ResponseEntity<?> get(@PathVariable String key) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver(redisIP, port);
			StringResponse sr = new StringResponse(key, jedis.get(key));
			return ResponseEntity.ok(sr);
		} catch (KeyValueNotFoundException e) {
			log.error(e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} catch (JedisDataException e) {
			log.error(e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	@PostMapping(value="/set/{key}/{value}")
	public ResponseEntity<?> set(@PathVariable("key") String key,
								@PathVariable("value") String value) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver(redisIP, port);
			StringResponse sr = new StringResponse(key, value);
			jedis.set(sr);
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(URI.create(String.format("/redis/set/%s", key)));
			return new ResponseEntity<StringResponse>(sr, headers, HttpStatus.CREATED);
		} catch (JedisDataException e) {
			log.error(e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	@PostMapping("/set")
	public ResponseEntity<?> set(@RequestBody StringResponse sr) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver(redisIP, port);
			jedis.set(sr);
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(URI.create(String.format("/redis/set/%s", sr.getKey())));
			return new ResponseEntity<StringResponse>(sr, headers, HttpStatus.CREATED);
		} catch (JedisDataException e) {
			log.error(e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	@DeleteMapping("/del/{key}")
	public ResponseEntity<?> del(@PathVariable String [] key) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver(redisIP, port);
			Long count = jedis.del(key);
			if (count == 0) {
				return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<Void>(HttpStatus.OK);
			}
		} catch(JedisDataException e) {
			log.error(e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
}
