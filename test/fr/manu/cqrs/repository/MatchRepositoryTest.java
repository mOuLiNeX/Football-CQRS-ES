package fr.manu.cqrs.repository;

import static fr.manu.cqrs.EventSourcingAsserter.givenEvents;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import fr.manu.cqrs.EventSourcingtTestRule;
import fr.manu.cqrs.domain.Match;
import fr.manu.cqrs.domain.MatchId;
import fr.manu.cqrs.domain.event.MatchCreatedEvent;

public class MatchRepositoryTest {
	@Rule
	public EventSourcingtTestRule defaults = new EventSourcingtTestRule();
	private MatchRepository repository;

	@Before
	public void setUp() {
		repository = new MatchRepository();
	}

	@Test
	public void canReadSavedMatchById() {
		// Given events
		MatchId id = MatchId.newMatchId();
		String home = "team1";
		String away = "team2";
		givenEvents(new MatchCreatedEvent(id, home, away));

		// When
		Match actualMatch = repository.find(id);

		// Then
		assertNotNull(actualMatch);
		assertEquals(actualMatch.id, id);
		assertEquals(actualMatch.getHomeTeamName(), home);
		assertEquals(actualMatch.getAwayTeamName(), away);
	}

	@Test
	public void cannotReadUnknownMatch() {
		// Given no event
		MatchId unknownId = MatchId.newMatchId();

		// When
		Match actualMatch = repository.find(unknownId);

		// Then
		assertNull(actualMatch);
	}

}
