package com.unai.app.springmongo.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.unai.app.springmongo.model.Restaurant;

public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
	
	public Restaurant findByName(String name);
	public List<Restaurant> findByBorough(String borough);
	public Page<Restaurant> findAll(Pageable pageable);
	
}
