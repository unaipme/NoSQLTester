package com.unai.app.mongo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Updates.combine;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.unai.app.config.DateSerializer;

public class Grade implements Documentable{
	
	@JsonSerialize(using=DateSerializer.class) private Date date;
	private String grade;
	private Double score;
	
	public Grade() {}
	
	public Grade date(Date date) {
		this.date = date;
		return this;
	}
	
	public Grade grade(String grade) {
		this.grade = grade;
		return this;
	}
	
	public Grade score(Double score) {
		this.score = score;
		return this;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}
	
	public static Grade fromDocument(Document d) {
		return new Grade()
				.date(d.getDate("date"))
				.grade(d.getString("grade"))
				.score(Double.valueOf(d.get("score").toString()));
	}

	@Override
	public Document toDocument() {
		return new Document()
			.append("date", date)
			.append("grade", grade)
			.append("score", score);
	}

	@Override
	public Bson updater() {
		List<Bson> list = new ArrayList<>();
		if (date != null) list.add(set("date", date));
		if (grade != null) list.add(set("grade", grade));
		if (score != null) list.add(set("score", score));
		return combine(list);
	}
	
}
