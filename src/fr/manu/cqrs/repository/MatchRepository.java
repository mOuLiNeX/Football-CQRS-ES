package fr.manu.cqrs.repository;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Maps;

import fr.manu.cqrs.domain.Match;
import fr.manu.cqrs.domain.MatchId;
import fr.manu.cqrs.events.MatchEvent;
import fr.manu.cqrs.events.MatchEventStore;

// TODO Abstraction avec generics
@Singleton
public class MatchRepository {

	@Inject
	MatchEventStore store;

	private final static Map<MatchId, Match> memDB = Maps.newHashMap();

	public Match find(MatchId id) {
		Match found = null;
		if (!memDB.containsKey(id)) {
			found = replay(id, store.getEvents(id));
			memDB.put(id, found);
		}
		return memDB.get(id);
	}

	/*
	 * To load this Aggregate from our Event Store, the Repository loads all
	 * Events that were applied to this particular Aggregate, and then applies
	 * them on the instance of the Aggregate in order they were generated.
	 */
	private Match replay(MatchId id, Collection<MatchEvent> events) {
		Match newMatch = null;

		if (!events.isEmpty()) {
			for (MatchEvent evt : events) {
				newMatch = evt.applyOn(newMatch);
			}
		}
		return newMatch;
	}

	public void init() {
		memDB.clear();
	}
}