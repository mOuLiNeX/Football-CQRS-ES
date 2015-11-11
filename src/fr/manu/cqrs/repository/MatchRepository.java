package fr.manu.cqrs.repository;

import java.util.Collection;
import java.util.Map;

import javax.inject.Singleton;

import com.google.common.collect.Maps;

import fr.manu.cqrs.domain.Match;
import fr.manu.cqrs.domain.MatchId;
import fr.manu.cqrs.domain.event.MatchEvent;
import fr.manu.cqrs.domain.event.MatchEventStore;

// TODO Abstraction
@Singleton
public class MatchRepository {
    private final static Map<MatchId, Match> memDB = Maps.newHashMap();

    public Match find(MatchId id) {
        Match found = null;
        if (!memDB.containsKey(id)) {
            found = load(id, MatchEventStore.getEvents(id));
            memDB.put(id, found);
        }
        return memDB.get(id);
    }

    /*
     * To load this Aggregate from our Event Store, the Repository loads all Events that were applied to this
     * particular Aggregate, and then applies them on the instance of the Aggregate in order they were
     * generated.
     */
    private Match load(MatchId id, Collection<MatchEvent> events) {
        Match newMatch = null;

        if (!events.isEmpty()) {
            newMatch = new Match(id);
            for (MatchEvent evt : events) {
                newMatch.handle(evt);
            }
        }
        return newMatch;
    }

    public static void init() {
        memDB.clear();
    }
}