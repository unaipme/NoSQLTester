package com.unai.app.neo4j.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.types.Node;
import org.springframework.stereotype.Service;

import com.unai.app.neo4j.model.Movie;
import com.unai.app.neo4j.model.Person;
import com.unai.app.neo4j.model.RelationType;

@Service
public class PersonService {
	
	public Set<Person> getAll(StatementResult result) {
		Set<Person> set = new HashSet<Person>();
		while (result.hasNext()) {
			Record r = result.next();
			Node personNode = r.get("person").asNode();
			String personName = personNode.get("name").asString();
			Person person = findPerson(set, personName);
			if (person == null) {
				person = Person.fromNode(personNode);
				set.add(person);
			}
			Movie movie = Movie.fromNode(r.get("movie").asNode());
			RelationType rt = RelationType.fromRelationship(r.get("relation").asRelationship());
			person.did(rt, movie);
		}
		return set;
	}
	
	private Person findPerson(Set<Person> set, String name) {
		Person p = null;
		for (Iterator<Person> it = set.iterator(); it.hasNext(); ) {
			Person a = it.next();
			if (a.getName().equals(name)) {
				p = a;
			}
		}
		return p;
	}
	
	public Person getOne(StatementResult result) {
		boolean personParsed = false;
		Person person = null;
		while (result.hasNext()) {
			Record r = result.next();
			if (!personParsed) {
				person = Person.fromNode(r.get("person").asNode());
				personParsed = true;
			}
			Movie movie = Movie.fromNode(r.get("movie").asNode());
			RelationType rt = RelationType.valueOf(r.get("relation").asRelationship().type());
			person.did(rt, movie);
		}
		return person;
	}
	
}
