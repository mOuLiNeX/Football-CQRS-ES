package fr.manu.cqrs.query;

import static fr.manu.cqrs.EventSourcingAsserter.givenEvents;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.Iterables;

import fr.manu.cqrs.EventSourcingAsserter;
import fr.manu.cqrs.EventSourcingtTestRule;
import fr.manu.cqrs.domain.MatchId;
import fr.manu.cqrs.domain.event.MatchCreatedEvent;
import fr.manu.cqrs.domain.event.MatchFinishedEvent;
import fr.manu.cqrs.domain.event.MatchStartedEvent;
import fr.manu.cqrs.service.QueryService;

public class MatchQueryTest {
	@Rule
	public EventSourcingtTestRule defaults = new EventSourcingtTestRule();
	private QueryService query;

	@Before
	public void setUp() {
		query = new QueryService();
	}

	@Test
	public void testQueryPlannedMatch() {
		final MatchId id = MatchId.newMatchId();
		// Given events
		givenEvents(new MatchCreatedEvent(id, "team1", "team2"));

		// When query
		Collection<MatchState> allMatches = query.getCurrentMatches();

		// Then expect states
		assertFalse(allMatches.isEmpty());
		assertTrue(Iterables.contains(allMatches, MatchState.createPlannedMatch(id)));
	}

	@Test
	public void testQueryStartedMatch() {
		final MatchId id = MatchId.newMatchId();
		// Given events
		givenEvents(new MatchCreatedEvent(id, "team1", "team2"), new MatchStartedEvent(id, LocalDateTime.now()));

		// When query
		Collection<MatchState> allMatches = query.getCurrentMatches();

		// Then expect states
		assertFalse(allMatches.isEmpty());
		assertTrue(Iterables.contains(allMatches, MatchState.createStartedMatch(id)));
	}

	@Test
	public void testQueryFinishedMatch() {
		final MatchId id = MatchId.newMatchId();
		// Given events
		givenEvents(new MatchCreatedEvent(id, "team1", "team2"), new MatchStartedEvent(id, LocalDateTime.now()),
				new MatchFinishedEvent(id, LocalDateTime.now(), 3, 0));

		// When query
		Collection<MatchState> allMatches = query.getCurrentMatches();

		// Then expect states
		assertTrue(allMatches.isEmpty());
	}

	@Test
	public void testQueryByMatch() {
		final MatchId id1 = MatchId.newMatchId();
		final MatchId id2 = MatchId.newMatchId();

		// Given events
		givenEvents(new MatchCreatedEvent(id1, "team1", "team2"), new MatchStartedEvent(id1, LocalDateTime.now()),
				new MatchCreatedEvent(id2, "team4", "team3"));

		// Then expect states
		assertEquals(MatchState.createStartedMatch(id1), query.getCurrentMatchById(id1));
		assertEquals(MatchState.createPlannedMatch(id2), query.getCurrentMatchById(id2));
	}
}
