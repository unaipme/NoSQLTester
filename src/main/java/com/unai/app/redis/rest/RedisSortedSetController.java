package com.unai.app.redis.rest;

import java.util.HashMap;
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
import com.unai.app.redis.exception.SetEmptyException;
import com.unai.app.redis.model.SetResponse;
import com.unai.app.utils.HTTPHeaders;

import io.swagger.annotations.ApiOperation;
import redis.clients.jedis.exceptions.JedisDataException;


@RestController
@RequestMapping("/redis")
public class RedisSortedSetController {
	
	private Logger log = LoggerFactory.getLogger(RedisSortedSetController.class);
	
	@GetMapping("/zrange/{key}/all")
	@ApiOperation(notes="public ResponseEntity<?> zrangeall(String)::36", value="Gets all elements in the sorted list with the given key.")
	public ResponseEntity<?> zrangeall(@PathVariable String key) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
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
	@ApiOperation(notes="public ResponseEntity<?> zrange(String, Long, Long)::60", value="Gets the range specified of the sorted list with the given key.")
	public ResponseEntity<?> zrange(@PathVariable("key") String key, @PathVariable("start") Long start, @PathVariable("stop") Long stop) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
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
	@ApiOperation(notes="public ResponseEntity<?> zadd(String, Double, String)::84", value="Adds a value with the given score to the sorted list with the given key.")
	public ResponseEntity<?> zadd(@PathVariable("key") String key, @PathVariable("score") Double score, @PathVariable("value") String value) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
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
	@ApiOperation(notes="public ResponseEntity<?> zadd(String, Map<String, Double>)::110", value="Adds the map of values with the corresponding score to the sorted list with given key.")
	public ResponseEntity<?> zadd(@PathVariable String key, @RequestBody Map<String, Double> map) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
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
	@ApiOperation(notes="public ResponseEntity<?> zrem(String, String [])::134", value="Deletes from the sorted list with the given key the specified values.")
	public ResponseEntity<?> zrem(@PathVariable("key") String key, @PathVariable("field") String [] fields) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
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
