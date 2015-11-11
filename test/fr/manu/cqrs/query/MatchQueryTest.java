package fr.manu.cqrs.query;

import static fr.manu.cqrs.EventSourcingAsserter.givenEvents;

import java.time.LocalDateTime;
import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import fr.manu.cqrs.EventSourcingtTestRule;
import fr.manu.cqrs.IoCInjectorRule;
import fr.manu.cqrs.domain.MatchId;
import fr.manu.cqrs.domain.event.MatchCreatedEvent;
import fr.manu.cqrs.domain.event.MatchFinishedEvent;
import fr.manu.cqrs.domain.event.MatchStartedEvent;
import fr.manu.cqrs.service.QueryService;

public class MatchQueryTest {
	@Rule
	public EventSourcingtTestRule defaults = new EventSourcingtTestRule();

	@ClassRule
	public static IoCInjectorRule inject = new IoCInjectorRule();

	@Test
	public void testQueryPlannedMatch() {
		final MatchId id = MatchId.newMatchId();
		// Given events
		givenEvents(new MatchCreatedEvent(id, "team1", "team2"));

		// When query
		QueryService query = inject.getInstance(QueryService.class);
		Collection<MatchState> allMatches = query.getCurrentMatches();

		// Then expect states
		Assertions.assertThat(allMatches).isNotEmpty().contains(MatchState.createPlannedMatch(id));
	}

	@Test
	public void testQueryStartedMatch() {
		final MatchId id = MatchId.newMatchId();
		// Given events
		givenEvents(new MatchCreatedEvent(id, "team1", "team2"), new MatchStartedEvent(id, LocalDateTime.now()));

		// When query
		QueryService query = inject.getInstance(QueryService.class);
		Collection<MatchState> allMatches = query.getCurrentMatches();

		// Then expect states
		Assertions.assertThat(allMatches).isNotEmpty().contains(MatchState.createStartedMatch(id));
	}

	@Test
	public void testQueryFinishedMatch() {
		final MatchId id = MatchId.newMatchId();
		// Given events
		givenEvents(new MatchCreatedEvent(id, "team1", "team2"), new MatchStartedEvent(id, LocalDateTime.now()),
				new MatchFinishedEvent(id, LocalDateTime.now(), 3, 0));

		// When query
		QueryService query = inject.getInstance(QueryService.class);
		Collection<MatchState> allMatches = query.getCurrentMatches();

		// Then expect states
		Assertions.assertThat(allMatches).isEmpty();
	}

	@Test
	public void testQueryByMatch() {
		final MatchId id1 = MatchId.newMatchId();
		final MatchId id2 = MatchId.newMatchId();

		// Given events
		givenEvents(new MatchCreatedEvent(id1, "team1", "team2"), new MatchStartedEvent(id1, LocalDateTime.now()),
				new MatchCreatedEvent(id2, "team4", "team3"));

		// Then expect states
		QueryService query = inject.getInstance(QueryService.class);
		Assertions.assertThat(query.getCurrentMatchById(id1)).isNotNull().isEqualTo(MatchState.createStartedMatch(id1));
		Assertions.assertThat(query.getCurrentMatchById(id2)).isNotNull().isEqualTo(MatchState.createPlannedMatch(id2));
	}
}
