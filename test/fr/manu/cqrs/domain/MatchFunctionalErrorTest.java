package fr.manu.cqrs.domain;

import static java.time.LocalDateTime.now;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import fr.manu.cqrs.EventSourcingtTestRule;
import fr.manu.cqrs.IoCInjectorRule;
import fr.manu.cqrs.domain.exception.MatchAlreadyFinishedException;
import fr.manu.cqrs.domain.exception.MatchAlreadyStartedException;
import fr.manu.cqrs.domain.exception.MatchNotStartedException;
import fr.manu.cqrs.service.MatchCommand;

public class MatchFunctionalErrorTest {
	@ClassRule
	public static final IoCInjectorRule INJECT = new IoCInjectorRule();

	@Rule
	public EventSourcingtTestRule eventSrcContext = new EventSourcingtTestRule(INJECT);

	@Test(expected = MatchAlreadyStartedException.class)
	public void testCannotStartMatchTwice() {
		MatchCommand command = INJECT.getInstance(MatchCommand.class);

		// Given
		MatchId id = command.createMatch("team1", "team2");
		command.startMatch(id, now());

		// When
		command.startMatch(id, now());

		// Then expect error
	}

	@Test(expected = MatchNotStartedException.class)
	public void testCannotFinishAMatchBeforeItStarts() {
		MatchCommand command = INJECT.getInstance(MatchCommand.class);

		// Given
		MatchId id = command.createMatch("team1", "team2");
		command.startMatch(id, now());

		// When command
		command.finishMatch(id, new Score(1, 0), now().minusHours(1));

		// Then expect error
	}

	@Test(expected = MatchAlreadyFinishedException.class)
	public void testCannotFinishMatchTwice() {
		MatchCommand command = INJECT.getInstance(MatchCommand.class);

		// Given
		MatchId id = command.createMatch("team1", "team2");
		command.startMatch(id, now());
		command.finishMatch(id, new Score(1, 0), now().plusMinutes(90));

		// When command
		command.finishMatch(id, new Score(1, 0), now().plusMinutes(90));

		// Then expect error
	}
}
