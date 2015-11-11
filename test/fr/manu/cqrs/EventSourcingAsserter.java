package fr.manu.cqrs;

import java.util.Collection;

import org.assertj.core.api.Assertions;

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
		Assertions.assertThat(evts).isNotNull().isNotEmpty().endsWith(expectedEvt);
    }

}
