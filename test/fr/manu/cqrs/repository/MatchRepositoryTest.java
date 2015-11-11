package fr.manu.cqrs.repository;

import static fr.manu.cqrs.EventSourcingAsserter.givenEvents;

import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import fr.manu.cqrs.EventSourcingtTestRule;
import fr.manu.cqrs.IoCInjectorRule;
import fr.manu.cqrs.domain.Match;
import fr.manu.cqrs.domain.MatchId;
import fr.manu.cqrs.domain.event.MatchCreatedEvent;

public class MatchRepositoryTest {
	@Rule
	public EventSourcingtTestRule defaults = new EventSourcingtTestRule();

	@ClassRule
	public static IoCInjectorRule inject = new IoCInjectorRule();

	@Test
	public void canReadSavedMatchById() {
		// Given events
		MatchId id = MatchId.newMatchId();
		String home = "team1";
		String away = "team2";
		givenEvents(new MatchCreatedEvent(id, home, away));

		// When
		MatchRepository repository = inject.getInstance(MatchRepository.class);
		Match actualMatch = repository.find(id);

		// Then
		Assertions.assertThat(actualMatch).isNotNull().matches((match) -> match.id.equals(id)
				&& match.getAwayTeamName().equals(away) && match.getHomeTeamName().equals(home));
	}

	@Test
	public void cannotReadUnknownMatch() {
		// Given no event
		MatchId unknownId = MatchId.newMatchId();

		// When
		MatchRepository repository = inject.getInstance(MatchRepository.class);
		Match actualMatch = repository.find(unknownId);

		// Then
		Assertions.assertThat(actualMatch).isNull();
	}

}
