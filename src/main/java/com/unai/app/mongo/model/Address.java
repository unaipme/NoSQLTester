package com.unai.app.mongo.model;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

public class Address implements Documentable {
	
	private String building;
	private String street;
	private String zipcode;
	private Double [] coord;
	
	public Address() {
		coord = new Double [2];
	}
	
	public Address zipcode(String zipcode) {
		this.zipcode = zipcode;
		return this;
	}
	
	public Address coord(Double x, Double y) {
		this.coord[0] = x;
		this.coord[1] = y;
		return this;
	}
	
	public Address street(String street) {
		this.street = street;
		return this;
	}
	
	public Address building(String building) {
		this.building = building;
		return this;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public Double[] getCoord() {
		return coord;
	}

	public void setCoord(Double[] coord) {
		this.coord = coord;
	}
	
	@SuppressWarnings("unchecked")
	public static Address fromDocument(Document d) {
		List<Double> coord = new ArrayList<>();
		if (d.get("coord") != null) {
			coord.add((Double)((List<Object>) d.get("coord")).get(0));
			coord.add((Double)((List<Object>) d.get("coord")).get(1));
		} else {
			coord.add(null); coord.add(null);
		}
		return new Address()
				.building(d.getString("building"))
				.coord(coord.get(0), coord.get(1))
				.street(d.getString("street"))
				.zipcode(d.getString("zipcode"));
	}

	@Override
	public Document toDocument() {
		return new Document()
				.append("building", building)
				.append("street", street)
				.append("zipcode", zipcode)
				.append("coord", Arrays.asList(coord));
	}

	@Override
	public Bson updater() {
		List<Bson> list = new ArrayList<>();
		if (building != null) list.add(set("building", building));
		if (street != null) list.add(set("street", street));
		if (zipcode != null) list.add(set("zipcode", zipcode));
		return combine(list);
	}
	
}
