package com.unai.app.redis.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unai.app.redis.JedisDriver;
import com.unai.app.redis.model.SetResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import redis.clients.jedis.exceptions.JedisDataException;

@RestController
@RequestMapping("/redis")
@Api
public class RedisSetController {
	private Logger log = LoggerFactory.getLogger(RedisSetController.class);
	
	@PostMapping("/sadd/{key}/{members}")
	@ApiOperation(notes="public ResponseEntity<?> sadd(String, String [])::29", value="Adds one or more values to the set with the given key.")
	public ResponseEntity<?> sadd(@PathVariable("key") String key, @PathVariable("members") String [] members) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
			jedis.sadd(key, members);
			SetResponse sr = new SetResponse(key, jedis.smembers(key));
			return ResponseEntity.ok(sr);
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
	
	@GetMapping("/smembers/{key}")
	@ApiOperation(notes="public ResponseEntity<?> smembers(String)::51", value="Gets all elements in the set with the given key.")
	public ResponseEntity<?> smembers(@PathVariable String key) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
			SetResponse sr = new SetResponse(key, jedis.smembers(key));
			return ResponseEntity.ok(sr);
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
	
	@DeleteMapping("/srem/{key}")
	@ApiOperation(notes="public ResponseEntity<?> sremall(String)::72", value="Deletes the set with the given key and all its elements.")
	public ResponseEntity<?> sremall(@PathVariable String key) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
			jedis.del(key);
			return new ResponseEntity<>(HttpStatus.OK);
		}  catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	@DeleteMapping("/srem/{key}/{members}")
	@ApiOperation(notes="public ResponseEntity<?> sremall(String, String[])::90", value="Deletes the said elements from the set with the given key.")
	public ResponseEntity<?> sremall(@PathVariable("key") String key, @PathVariable("field") String [] members) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
			jedis.srem(key, members);
			return new ResponseEntity<>(HttpStatus.OK);
		}  catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
}
