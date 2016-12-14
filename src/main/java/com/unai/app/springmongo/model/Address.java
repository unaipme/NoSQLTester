package com.unai.app.springmongo.model;

public class Address {
	
	private String building;
	private String street;
	private String zipcode;
	private Double [] coord;
	
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
	
	
}
