package com.unai.app.redis.rest;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unai.app.redis.JedisDriver;
import com.unai.app.utils.HTTPHeaders;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/redis")
@Api
public class RedisHyperLogLogController {
	
	private Logger log = LoggerFactory.getLogger(RedisHyperLogLogController.class);
	
	@GetMapping("/pfcount/{key}")
	@ApiOperation(notes="public ResponseEntity<?> pfcount(String)::32", value="Returns the approximated cardinality of the hyperloglog with the given key.")
	public ResponseEntity<?> pfcount(@PathVariable String key) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
			Map<String, Long> response = new HashMap<>();
			response.put("created", jedis.pfcount(key));
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
	
	@PostMapping("/pfadd/{key}/{elements}")
	@ApiOperation(notes="public ResponseEntity<?> pfadd(String, String [])::51", value="Adds the specified elements to the hyperloglog with the given key.")
	public ResponseEntity<?> pfadd(@PathVariable("key") String key, @PathVariable("elements") String [] elems) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
			Map<String, Long> response = new HashMap<>();
			response.put("created", jedis.pfadd(key, elems));
			HTTPHeaders h = new HTTPHeaders().location(String.format("/redis/pfcount/%s", key));
			return new ResponseEntity<Map<String, Long>>(response, h, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	@PutMapping("/pfmerge/{destkey}/{sourcekey}")
	@ApiOperation(notes="public ResponseEntity<?> pfmerge(String, String)::71", value="Merges two hyperloglogs into one.")
	public ResponseEntity<?> pfmerge(@PathVariable("destkey") String destKey, @PathVariable("sourcekey") String sourceKey) {
		JedisDriver jedis = null;
		try {
			jedis = new JedisDriver();
			jedis.pfmerge(destKey, sourceKey);
			return new ResponseEntity<>(HttpStatus.OK);
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
