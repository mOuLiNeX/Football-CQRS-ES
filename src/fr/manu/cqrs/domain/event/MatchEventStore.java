package fr.manu.cqrs.domain.event;

import java.util.Collection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import fr.manu.cqrs.domain.MatchId;

// TODO A bouger (code infra)
public final class MatchEventStore {

    private static final Multimap<MatchId, MatchEvent> store = ArrayListMultimap.create();

    public static void append(MatchEvent... events) {
        for (MatchEvent evt : events) {
            store.put(evt.getId(), evt);
        }
    }

    public static Collection<MatchEvent> getEvents(MatchId id) {
        return store.get(id);
    }

    public static void init() {
        store.clear();
    }

}
