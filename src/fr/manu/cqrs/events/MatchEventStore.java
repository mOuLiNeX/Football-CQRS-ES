package fr.manu.cqrs.events;

import java.util.Collection;

import javax.inject.Singleton;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.Subscribe;

import fr.manu.cqrs.domain.MatchId;

@Singleton
public final class MatchEventStore {

	private final Multimap<MatchId, MatchEvent> store = ArrayListMultimap.create();

	@Subscribe
	public void receivePublishedEvent(MatchEvent evt) {
		append(evt);
	}

	private void append(MatchEvent... events) {
        for (MatchEvent evt : events) {
			store.put(evt.matchId, evt);
        }
    }

	public Collection<MatchEvent> getEvents(MatchId id) {
        return store.get(id);
    }

	public void init() {
        store.clear();
    }

}
