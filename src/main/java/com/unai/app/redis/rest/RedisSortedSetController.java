package com.unai.app.redis.rest;

import java.util.HashMap;
import java.util.Map;

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
import com.unai.app.redis.exception.SetEmptyException;
import com.unai.app.redis.model.SetResponse;
import com.unai.app.utils.HTTPHeaders;

import redis.clients.jedis.exceptions.JedisDataException;


@RestController
@RequestMapping("/redis")
public class RedisSortedSetController {
	
	@Value("${redis.server.ip}")
	private String redisIP;
	
	@Value("${redis.server.port}")
	private int port;
	
	private Logger log = LoggerFactory.getLogger(RedisSortedSetController.class);
	
	@GetMapping("/zrange/{key}/all")
	public ResponseEntity<?> zrangeall(@PathVariable String key) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver(redisIP, port);
			SetResponse sr = new SetResponse(key, jedis.zrange(key, 0, -1));
			return new ResponseEntity<SetResponse>(sr, HttpStatus.OK);
		} catch (JedisDataException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} catch (SetEmptyException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	@GetMapping("/zrange/{key}/{start}/{stop}")
	public ResponseEntity<?> zrange(@PathVariable("key") String key, @PathVariable("start") Long start, @PathVariable("stop") Long stop) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver(redisIP, port);
			SetResponse sr = new SetResponse(key, jedis.zrange(key, start, stop));
			return ResponseEntity.ok(sr);
		} catch (JedisDataException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} catch (SetEmptyException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	@PostMapping("/zadd/{key}/{score}/{value}")
	public ResponseEntity<?> zadd(@PathVariable("key") String key, @PathVariable("score") Double score, @PathVariable("value") String value) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver(redisIP, port);
			Map<String, Double> map = new HashMap<>();
			map.put(value, score);
			Long created = jedis.zadd(key, map);
			Map<String, Long> response = new HashMap<>();
			response.put("created", created);
			HttpHeaders h = new HTTPHeaders().location(String.format("/redis/zrange/%s/all",key));
			return new ResponseEntity<Map<String, Long>>(response, h, HttpStatus.OK);
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
	
	@PostMapping(value="/zadd/{key}", consumes={"application/json"})
	public ResponseEntity<?> zadd(@PathVariable String key, @RequestBody Map<String, Double> map) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver(redisIP, port);
			Long created = jedis.zadd(key, map);
			Map<String, Long> response = new HashMap<>();
			response.put("created", created);
			HttpHeaders h = new HTTPHeaders().location(String.format("/redis/zrange/%s/all", key));
			return new ResponseEntity<Map<String, Long>>(response, h, HttpStatus.OK);
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
	
	@DeleteMapping("/zrem/{key}/{field}")
	public ResponseEntity<?> zrem(@PathVariable("key") String key, @PathVariable("field") String [] fields) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver(redisIP, port);
			Long deleted = jedis.zrem(key, fields);
			Map<String, Long> response = new HashMap<>();
			response.put("deleted", deleted);
			return ResponseEntity.ok(response);
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
