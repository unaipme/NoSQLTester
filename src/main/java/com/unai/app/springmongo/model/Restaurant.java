package com.unai.app.springmongo.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="restaurants")
public class Restaurant {
	
	@Id private String id;
	private String borough;
	private String cuisine;
	private String name;
	private String restaurant_id;
	private Address address;
	private List<Grade> grades;

	public Restaurant() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBorough() {
		return borough;
	}

	public void setBorough(String borough) {
		this.borough = borough;
	}

	public String getCuisine() {
		return cuisine;
	}

	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRestaurant_id() {
		return restaurant_id;
	}

	public void setRestaurant_id(String restaurant_id) {
		this.restaurant_id = restaurant_id;
	}
	
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	public List<Grade> getGrades() {
		return grades;
	}

	public void setGrades(List<Grade> grades) {
		this.grades = grades;
	}

	public void set(String name, Object value) {
		switch (name) {
		case "borough": setBorough((String) value);
		break;
		case "cuisine": setCuisine((String) value);
		break;
		case "name": setName((String) name);
		break;
		case "restaurant_id": setRestaurant_id((String) value);
		break;
		}
	}

}
