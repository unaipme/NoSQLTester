package com.unai.app.redis.rest;

import java.util.HashMap;

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
import com.unai.app.redis.model.ListResponse;
import com.unai.app.redis.model.StringResponse;

import redis.clients.jedis.exceptions.JedisDataException;

@RestController
@RequestMapping("/rest")
public class RedisListController {
	
	@Value("${redis.server.ip}")
	private String redisIP;
	
	@Value("${redis.server.port}")
	private int port;
	
	private Logger log = LoggerFactory.getLogger(RedisListController.class);
	
	@PostMapping("/lpush/{key}/{values}")
	public ResponseEntity<?> lpush(@PathVariable("key") String key, @PathVariable("values") String [] values) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver(redisIP, port);
			Long created = jedis.lpush(key, values);
			HashMap<String, Long> response = new HashMap<>();
			response.put("created", created);
			return ResponseEntity.ok(response);
		} catch (JedisDataException e) {
			log.error(e.getMessage(), e);
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
	
	@GetMapping("/lindex/{key}/{index}")
	public ResponseEntity<?> lindex(@PathVariable("key") String key, @PathVariable("index") Long index) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver(redisIP, port);
			StringResponse sr = new StringResponse(String.format("%s[%d]", key, index), jedis.lindex(key, index));
			return ResponseEntity.ok(sr);
		} catch (JedisDataException e) {
			log.error(e.getMessage(), e);
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
	
	@GetMapping("/lindex/{key}/all")
	public ResponseEntity<?> lall(@PathVariable String key) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver(redisIP, port);
			ListResponse lr = new ListResponse(key, jedis.lrange(key, 0L, -1L));
			return ResponseEntity.ok(lr);
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
	
	@DeleteMapping("/lrem/{key}/all")
	public ResponseEntity<?> lremall(@PathVariable String key) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver(redisIP, port);
			jedis.del(key);
			return new ResponseEntity<Void>(HttpStatus.OK);
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
	
	@DeleteMapping("/lrem/{key}/{value}/{count}")
	public ResponseEntity<?> lrem(@PathVariable("key") String key, @PathVariable(value="count", required=false) Long count, @PathVariable("value") String value) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver(redisIP, port);
			jedis.lrem(key, count, value);
			return new ResponseEntity<Void>(HttpStatus.OK);
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
	
}
