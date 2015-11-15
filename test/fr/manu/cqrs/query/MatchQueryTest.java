package fr.manu.cqrs.query;

import java.time.LocalDateTime;
import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import fr.manu.cqrs.EventSourcingtTestRule;
import fr.manu.cqrs.IoCInjectorRule;
import fr.manu.cqrs.domain.MatchId;
import fr.manu.cqrs.events.MatchCreatedEvent;
import fr.manu.cqrs.events.MatchFinishedEvent;
import fr.manu.cqrs.events.MatchStartedEvent;
import fr.manu.cqrs.service.AllQueries;

public class MatchQueryTest {
	@ClassRule
	public static final IoCInjectorRule INJECT = new IoCInjectorRule();

	@Rule
	public EventSourcingtTestRule eventSrcContext = new EventSourcingtTestRule(INJECT);

	@Test
	public void testQueryPlannedMatch() {
		final MatchId id = MatchId.newMatchId();
		// Given events
		eventSrcContext.givenEvents(new MatchCreatedEvent(id, "team1", "team2"));

		// When query
		AllQueries query = INJECT.getInstance(AllQueries.class);
		Collection<MatchState> allMatches = query.getCurrentMatches();

		// Then expect states
		Assertions.assertThat(allMatches).isNotEmpty().contains(MatchState.createPlannedMatch(id));
	}

	@Test
	public void testQueryStartedMatch() {
		final MatchId id = MatchId.newMatchId();
		// Given events
		eventSrcContext.givenEvents(new MatchCreatedEvent(id, "team1", "team2"),
				new MatchStartedEvent(id, LocalDateTime.now()));

		// When query
		AllQueries query = INJECT.getInstance(AllQueries.class);
		Collection<MatchState> allMatches = query.getCurrentMatches();

		// Then expect states
		Assertions.assertThat(allMatches).isNotEmpty().contains(MatchState.createStartedMatch(id));
	}

	@Test
	public void testQueryFinishedMatch() {
		final MatchId id = MatchId.newMatchId();
		// Given events
		eventSrcContext.givenEvents(new MatchCreatedEvent(id, "team1", "team2"),
				new MatchStartedEvent(id, LocalDateTime.now()),
				new MatchFinishedEvent(id, LocalDateTime.now(), 3, 0));

		// When query
		AllQueries query = INJECT.getInstance(AllQueries.class);
		Collection<MatchState> allMatches = query.getCurrentMatches();

		// Then expect states
		Assertions.assertThat(allMatches).isEmpty();
	}

	@Test
	public void testQueryByMatch() {
		final MatchId id1 = MatchId.newMatchId();
		final MatchId id2 = MatchId.newMatchId();

		// Given events
		eventSrcContext.givenEvents(new MatchCreatedEvent(id1, "team1", "team2"),
				new MatchStartedEvent(id1, LocalDateTime.now()),
				new MatchCreatedEvent(id2, "team4", "team3"));

		// Then expect states
		AllQueries query = INJECT.getInstance(AllQueries.class);
		Assertions.assertThat(query.getCurrentMatchById(id1)).isNotNull().isEqualTo(MatchState.createStartedMatch(id1));
		Assertions.assertThat(query.getCurrentMatchById(id2)).isNotNull().isEqualTo(MatchState.createPlannedMatch(id2));
	}
}
