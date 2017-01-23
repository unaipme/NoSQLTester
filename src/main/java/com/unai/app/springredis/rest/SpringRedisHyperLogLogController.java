package com.unai.app.springredis.rest;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unai.app.springredis.service.HyperLogLogRepository;
import com.unai.app.utils.HTTPHeaders;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/sredis")
public class SpringRedisHyperLogLogController {
	
	@Autowired
	private HyperLogLogRepository hllRepository;
	
	private Logger log = LoggerFactory.getLogger(SpringRedisHyperLogLogController.class);
	
	@GetMapping("/pfcount/{key}")
	@ApiOperation(notes="", value="Returns the approximated cardinality of the hyperloglog with the given key.")
	public ResponseEntity<?> pfcount(@PathVariable String key) {
		try {
			Map<String, Long> response = new HashMap<>();
			response.put("count", hllRepository.pfcount(key));
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/pfcount/{key}/{elements}")
	@ApiOperation(notes="", value="Adds the specified elements to the hyperloglog with the given key.")
	public ResponseEntity<?> pfadd(@PathVariable("key") String key, @PathVariable("elements") String [] elems) {
		try {
			Map<String, Long> response = new HashMap<>();
			response.put("created", hllRepository.pfadd(key, elems));
			HttpHeaders h = new HTTPHeaders().location(String.format("/sredis/pfcount/%s", key));
			return new ResponseEntity<Map<String, Long>>(response, h, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/pfmerge/{destkey}/{sourcekey}")
	@ApiOperation(notes="", value="Merges two hyperloglogs into one.")
	public ResponseEntity<?> pfmerge(@PathVariable("destkey") String destKey, @PathVariable("sourcekey") String sourceKey) {
		try {
			hllRepository.pfmerge(destKey, sourceKey);
			HttpHeaders h = new HTTPHeaders().location(String.format("/sredis/pfcount/%s", destKey));
			return new ResponseEntity<>(h, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
