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
public class MovieService implements Neo4jService<Movie> {
	
	public Set<Movie> getAll(StatementResult result) {
		Set<Movie> set = new HashSet<Movie>();
		while (result.hasNext()) {
			Record r = result.next();
			Node movieNode = r.get("movie").asNode();
			Movie movie = findMovie(set, movieNode.get("title").asString(), movieNode.get("released").asInt());
			if (movie == null) {
				movie = Movie.fromNode(movieNode);
				set.add(movie);
			}
			Person person = Person.fromNode(r.get("person").asNode());
			RelationType rt = RelationType.fromRelationship(r.get("relation").asRelationship());
			movie.doneBy(rt, person);
		}
		return set;
	}
	
	private Movie findMovie(Set<Movie> set, String title, Integer released) {
		Movie ret = null;
		for (Iterator<Movie> it = set.iterator(); it.hasNext();) {
			Movie a = it.next();
			if (a.getTitle().equals(title) && a.getReleased().equals(released)) {
				ret = a;
			}
		}
		return ret;
	}

	public Movie getOne(StatementResult result) {
		boolean movieParsed = false;
		Movie movie = null;
		while (result.hasNext()) {
			Record r = result.next();
			if (!movieParsed) {
				movie = Movie.fromNode(r.get("movie").asNode());
				movieParsed = true;
			}
			if (!r.get("person").isNull()) {
				Person person = Person.fromNode(r.get("person").asNode());
				RelationType rt = RelationType.valueOf(r.get("relation").asRelationship().type());
				movie.doneBy(rt, person);
			}
		}
		return movie;
	}

	@Override
	public String createQuery(Movie m) throws NotEnoughInformationException {
		Properties data = Properties.createMatchProperties();
		if (!m.titleIsNull()) {
			data.add("title", m.getTitle());
		}
		if (!m.releasedIsNull()) {
			data.add("released", m.getReleased());
		}
		if (!m.taglineIsNull()) {
			data.add("tagline", m.getTagline());
		}
		if (data.isEmpty()) {
			throw new NotEnoughInformationException("movie");
		}
		return String.format("CREATE (movie:Movie %s)", data.parse());
	}

	@Override
	public String deleteQuery(Properties properties) {
		properties.setPrefix("m");
		return String.format("MATCH (m:Movie) WHERE %s DETACH DELETE m", properties.parse());
	}

	@Override
	public String selectWhereQuery(Properties properties) {
		properties.setPrefix("movie");
		return String.format("MATCH (movie:Movie) WHERE %s OPTIONAL MATCH (movie)<-[relation]-(person:Person) RETURN movie, relation, person", properties.parse());
	}

	@Override
	public String updateQuery(String where, String set) {
		String [] whereClauses = where.split(",");
		Properties defWhere = Properties.createMatchProperties();
		String [] setClauses = set.split("=");
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
		defSet.setPrefix("movie");
		return String.format("MATCH (movie:Movie %s) SET %s", defWhere.parse(), String.join(", ", defSet));
	}

}
