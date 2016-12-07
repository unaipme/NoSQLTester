package com.unai.app.redis.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

import redis.clients.jedis.exceptions.JedisDataException;

@RestController
@RequestMapping("/redis")
public class RedisSetController {
	
	@Value("${redis.server.ip}")
	private String redisIP;
	
	@Value("${redis.server.port}")
	private int port;
	
	private Logger log = LoggerFactory.getLogger(RedisSetController.class);
	
	@PostMapping("/sadd/{key}/{members}")
	public ResponseEntity<?> sadd(@PathVariable("key") String key, @PathVariable("members") String [] members) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver(redisIP, port);
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
	public ResponseEntity<?> smembers(@PathVariable String key) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver(redisIP, port);
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
	public ResponseEntity<?> sremall(@PathVariable String key) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver(redisIP, port);
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
	public ResponseEntity<?> sremall(@PathVariable("key") String key, @PathVariable("field") String [] members) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver(redisIP, port);
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
