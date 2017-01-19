package com.unai.app.neo4j.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.types.Node;
import org.springframework.stereotype.Service;

import com.unai.app.neo4j.exception.NotEnoughInformationException;
import com.unai.app.neo4j.model.Movie;
import com.unai.app.neo4j.model.Person;
import com.unai.app.neo4j.model.Properties;
import com.unai.app.neo4j.model.RelationType;

@Service
public class PersonService implements Neo4jService<Person> {
	
	@Override
	public Set<Person> getAll(StatementResult result) {
		Set<Person> set = new HashSet<Person>();
		while (result.hasNext()) {
			Record r = result.next();
			Node personNode = r.get("person").asNode();
			Person person = findPerson(set, personNode.get("name").asString());
			if (person == null) {
				person = Person.fromNode(personNode);
				set.add(person);
			}
			if (!r.get("movie").isNull()) {
				Movie movie = Movie.fromNode(r.get("movie").asNode());
				RelationType rt = RelationType.fromRelationship(r.get("relation").asRelationship());
				person.did(rt, movie);
			}
		}
		return set;
	}
	
	private Person findPerson(Set<Person> set, String personName) {
		Person ret = null;
		for (Iterator<Person> it = set.iterator(); it.hasNext(); ) {
			Person a = it.next();
			if (a.getName().equals(personName)) {
				ret = a;
			}
		}
		return ret;
	}
	
	@Override
	public Person getOne(StatementResult result) {
		boolean personParsed = false;
		Person person = null;
		while (result.hasNext()) {
			Record r = result.next();
			if (!personParsed) {
				person = Person.fromNode(r.get("person").asNode());
				personParsed = true;
			}
			if (!r.get("movie").isNull()) {
				Movie movie = Movie.fromNode(r.get("movie").asNode());
				RelationType rt = RelationType.valueOf(r.get("relation").asRelationship().type());
				person.did(rt, movie);
			}
		}
		return person;
	}
	
	public String updateQuery(String where, String set) {
		String [] whereClauses = where.split(",");
		Properties defWhere = Properties.createMatchProperties();
		String [] setClauses = set.split(",");
		Properties defSet = Properties.createWhereProperties();
		for (String s : whereClauses) {
			String [] property = s.split("=");
			if (property[1].matches(isNumeric)) {
				defWhere.add(property[0], Double.valueOf(property[1]));
			} else {
				defWhere.add(property[0], property[1]);
			}
		}
		for (String s : setClauses) {
			String [] property = s.split("=");
			if (property[1].matches(isNumeric)) {
				defSet.add(property[0], Double.valueOf(property[1]));
			} else {
				defSet.add(property[0], property[1]);
			}
		}
		defSet.setPrefix("person");
		return String.format("MATCH (person:Person %s) SET %s", defWhere.parse(), String.join(", ", defSet));
	}

	@Override
	public String createQuery(Person person) throws NotEnoughInformationException {
		Properties data = Properties.createMatchProperties();
		if (!person.nameIsNull()) {
			data.add("name", person.getName());
		}
		if (!person.bornIsNull()) {
			data.add("born", person.getBorn());
		}
		if (data.isEmpty()) {
			throw new NotEnoughInformationException("person");
		}
		return String.format("CREATE (person:Person %s)", data.parse());
	}

	@Override
	public String deleteQuery(Properties properties) {
		properties.setPrefix("p");
		return String.format("MATCH (p:Person) WHERE %s DETACH DELETE p", properties.parse());
	}

	@Override
	public String selectWhereQuery(Properties properties) { 
		properties.setPrefix("person");
		if (properties.isEmpty()) {
			return "MATCH (person:Person) OPTIONAL MATCH (person)-[relation]->(movie:Movie) RETURN person, relation, movie";
		} else {
			return String.format("MATCH (person:Person) WHERE %s OPTIONAL MATCH (person)-[relation]->(movie:Movie) RETURN person, relation, movie", properties.parse());
		}
	}
	
}
