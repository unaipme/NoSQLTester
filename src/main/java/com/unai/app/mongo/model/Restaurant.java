package com.unai.app.mongo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Updates.combine;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.unai.app.mongo.ObjectIdSerializer;

public class Restaurant implements Documentable {
	
	private static Function<Document, Grade> docToGrade = (d) -> {return Grade.fromDocument(d);};
	
	private static Function<Grade, Document> gradeToDoc = (g) -> {return g.toDocument();};
	
	@JsonSerialize(using=ObjectIdSerializer.class) private ObjectId _id;
	private String borough;
	private String cuisine;
	private String name;
	private String restaurant_id;
	private Address address;
	private List<Grade> grades;
	
	public Restaurant() {
		grades = new ArrayList<>();
	}
	
	public Restaurant _id(ObjectId _id) {
		this._id = _id;
		return this;
	}
	
	public Restaurant borough(String borough) {
		this.borough = borough;
		return this;
	}
	
	public Restaurant cuisine(String cuisine) {
		this.cuisine = cuisine;
		return this;
	}
	
	public Restaurant name(String name) {
		this.name = name;
		return this;
	}
	
	public Restaurant restaurantId(String id) {
		this.restaurant_id = id;
		return this;
	}
	
	public Restaurant address(Address address) {
		this.address = address;
		return this;
	}
	
	public Restaurant withGrade(Grade grade) {
		grades.add(grade);
		return this;
	}
	
	public Restaurant withGrades(List<Grade> grades) {
		setGrades(grades);
		return this;
	}

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
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
	
	@SuppressWarnings("unchecked")
	public static Restaurant fromDocument(Document d) {
		List<Document> grades = (List<Document>) d.get("grades");
		return new Restaurant()
				._id(d.getObjectId("_id"))
				.address(Address.fromDocument((Document) d.get("address")))
				.borough(d.getString("borough"))
				.cuisine(d.getString("cuisine"))
				.withGrades(grades.stream().map(docToGrade).collect(Collectors.toList()))
				.name(d.getString("name"))
				.restaurantId(d.getString("restaurant_id"));
	}

	@Override
	public Document toDocument() {
		Document d = new Document()
					.append("borough", borough)
					.append("cuisine", cuisine)
					.append("name", name)
					.append("restaurant_id", restaurant_id);
		if (_id != null) d.append("_id", _id);
		if (grades != null) d.append("grades", grades.stream().map(gradeToDoc).collect(Collectors.toList()));
		if (address != null) d.append("address", address.toDocument());
		return d;
	}

	@Override
	public Bson updater() {
		List<Bson> list = new ArrayList<>();
		if (borough != null) list.add(set("borough", borough));
		if (cuisine != null) list.add(set("cuisine", cuisine));
		if (name != null) list.add(set("name", name));
		if (restaurant_id != null) list.add(set("restaurant_id", restaurant_id));
		if (address != null) list.add(set("address", address.toDocument()));
		return combine(list);
	}
	
}
