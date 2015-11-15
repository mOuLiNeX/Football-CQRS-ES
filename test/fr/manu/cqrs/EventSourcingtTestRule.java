package fr.manu.cqrs;

import java.util.Collection;

import org.junit.rules.ExternalResource;

import fr.manu.cqrs.domain.MatchId;
import fr.manu.cqrs.events.MatchEvent;
import fr.manu.cqrs.events.MatchEventBus;
import fr.manu.cqrs.events.MatchEventStore;
import fr.manu.cqrs.query.MatchQuery;
import fr.manu.cqrs.query.MatchStateHandler;
import fr.manu.cqrs.query.TeamStateHandler;
import fr.manu.cqrs.query.TeamStatisticsQuery;
import fr.manu.cqrs.repository.MatchRepository;

public class EventSourcingtTestRule extends ExternalResource {

	private IoCInjectorRule inject;
	private MatchEventBus bus;
	private MatchEventStore evtStore;

	public EventSourcingtTestRule(IoCInjectorRule inject) {
		this.inject = inject;
		evtStore = inject.getInstance(MatchEventStore.class);
		bus = inject.getInstance(MatchEventBus.class);
	}

	@Override
	protected void before() throws Throwable {
		evtStore.init();
		inject.getInstance(MatchRepository.class).init();
		inject.getInstance(TeamStatisticsQuery.class).init();
		inject.getInstance(MatchQuery.class).init();
		bus.register(evtStore, inject.getInstance(TeamStateHandler.class), inject.getInstance(MatchStateHandler.class));
	}

	public void givenEvents(MatchEvent... events) {
		for (MatchEvent evt : events) {
			bus.publishEvent(evt);
		}
	}

	public Collection<MatchEvent> getEvents(MatchId id) {
		return inject.getInstance(MatchEventStore.class).getEvents(id);
	}
}
