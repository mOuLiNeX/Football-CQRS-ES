package fr.manu.cqrs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import com.google.common.collect.Iterables;

import fr.manu.cqrs.domain.event.MatchEvent;
import fr.manu.cqrs.domain.event.MatchEventBus;
import fr.manu.cqrs.domain.event.MatchEventStore;

public class EventSourcingAsserter {

    public static void givenEvents(MatchEvent... events) {
        for (MatchEvent evt : events) {
            MatchEventBus.publishEvent(evt);
        }
    }

    public static void expectEvent(MatchEvent expectedEvt) {
        Collection<MatchEvent> evts = MatchEventStore.getEvents(expectedEvt.getId());
        assertNotNull(evts);
        assertTrue(!evts.isEmpty());
        MatchEvent actualEvt = Iterables.getLast(evts);
        assertEquals(expectedEvt, actualEvt);

    }

}
