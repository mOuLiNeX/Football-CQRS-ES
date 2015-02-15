package fr.manu.cqrs;

import org.junit.rules.ExternalResource;

import fr.manu.cqrs.domain.event.MatchEventBus;
import fr.manu.cqrs.domain.event.MatchEventPersisterSubscriber;
import fr.manu.cqrs.domain.event.MatchEventStore;
import fr.manu.cqrs.query.MatchQuery;
import fr.manu.cqrs.query.MatchStateHandler;
import fr.manu.cqrs.query.TeamStateHandler;
import fr.manu.cqrs.query.TeamStatisticsQuery;
import fr.manu.cqrs.repository.MatchRepository;

public class EventSourcingtTestRule extends ExternalResource {

	@Override
	protected void before() throws Throwable {
		MatchEventStore.init();
		MatchRepository.init();
		TeamStatisticsQuery.init();
		MatchQuery.init();
		MatchEventBus.register(new MatchEventPersisterSubscriber(),
				new TeamStateHandler(), new MatchStateHandler());
	}
}
