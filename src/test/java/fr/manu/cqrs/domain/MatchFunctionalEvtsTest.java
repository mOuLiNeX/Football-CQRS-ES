package fr.manu.cqrs.domain;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import fr.manu.cqrs.EventSourcingtTestRule;
import fr.manu.cqrs.IoCInjectorRule;
import fr.manu.cqrs.domain.exception.MatchAlreadyFinishedException;
import fr.manu.cqrs.domain.exception.MatchAlreadyStartedException;
import fr.manu.cqrs.domain.exception.MatchNotStartedException;
import fr.manu.cqrs.events.MatchCreatedEvent;
import fr.manu.cqrs.events.MatchFinishedEvent;
import fr.manu.cqrs.events.MatchStartedEvent;
import fr.manu.cqrs.service.MatchCommand;

public class MatchFunctionalEvtsTest {
	@ClassRule
	public static final IoCInjectorRule INJECT = new IoCInjectorRule();

	@Rule
	public EventSourcingtTestRule eventSrcContext = new EventSourcingtTestRule(INJECT);

	@Test
	public void testCanDefineMatchWithTwoTeams() {
		String home = "team1";
		String away = "team2";

		// When command
		MatchCommand command = INJECT.getInstance(MatchCommand.class);
		MatchId id = command.createMatch(home, away);

		// Then expect event
		Assertions.assertThat(eventSrcContext.getEvents(id)).isNotNull().isNotEmpty()
				.endsWith(new MatchCreatedEvent(id, home, away));
	}

	@Test
	public void testCanStartMatchAtGivenTime() {
		MatchId id = MatchId.newMatchId();
		LocalDateTime startMatchLocalDateTime = LocalDateTime.now();

		// Given events
		eventSrcContext.givenEvents(new MatchCreatedEvent(id, "team1", "team2"));

		// When command
		MatchCommand command = INJECT.getInstance(MatchCommand.class);
		command.startMatch(id, startMatchLocalDateTime);

		// Then expect event
		Assertions.assertThat(eventSrcContext.getEvents(id)).isNotNull().isNotEmpty()
				.endsWith(new MatchStartedEvent(id, startMatchLocalDateTime));
	}

	@Test(expected = MatchAlreadyStartedException.class)
	public void testCannotStartMatchTwice() {
		MatchId id = MatchId.newMatchId();
		LocalDateTime startMatchLocalDateTime = LocalDateTime.now();

		// Given events
		eventSrcContext.givenEvents(new MatchCreatedEvent(id, "team1", "team2"),
				new MatchStartedEvent(id, startMatchLocalDateTime));

		// When command
		MatchCommand command = INJECT.getInstance(MatchCommand.class);
		command.startMatch(id, startMatchLocalDateTime);

		// Then expect error
	}

	@Test
	public void testCanFinishAMatchWithScore() {
		MatchId id = MatchId.newMatchId();
		LocalDateTime startMatchLocalDateTime = LocalDateTime.now();
		LocalDateTime endMatchLocalDateTime = LocalDateTime.now();
		Score score = new Score(1, 0);

		// Given events
		eventSrcContext.givenEvents(new MatchCreatedEvent(id, "team1", "team2"),
				new MatchStartedEvent(id, startMatchLocalDateTime));

		// When command
		MatchCommand command = INJECT.getInstance(MatchCommand.class);
		command.finishMatch(id, score, endMatchLocalDateTime);

		// Then expect event
		Assertions.assertThat(eventSrcContext.getEvents(id)).isNotNull().isNotEmpty()
				.endsWith(new MatchFinishedEvent(id, endMatchLocalDateTime, score.homeGoals, score.awayGoals));
	}

	@Test(expected = MatchNotStartedException.class)
	public void testCannotFinishAMatchBeforeItStarts() {
		MatchId id = MatchId.newMatchId();
		LocalDateTime oneHourBefore = LocalDateTime.now().minusHours(1);

		// Given events
		eventSrcContext.givenEvents(new MatchCreatedEvent(id, "team1", "team2"),
				new MatchStartedEvent(id, LocalDateTime.now()));

		// When command
		MatchCommand command = INJECT.getInstance(MatchCommand.class);
		command.finishMatch(id, new Score(1, 0), oneHourBefore);

		// Then expect error
	}

	@Test(expected = MatchAlreadyFinishedException.class)
	public void testCannotFinishMatchTwice() {
		MatchId id = MatchId.newMatchId();
		LocalDateTime oneHourAfter = LocalDateTime.now().plusHours(1);

		// Given events
		eventSrcContext.givenEvents(new MatchCreatedEvent(id, "team1", "team2"),
				new MatchStartedEvent(id, LocalDateTime.now()), new MatchFinishedEvent(id, oneHourAfter, 1, 0));

		// When command
		MatchCommand command = INJECT.getInstance(MatchCommand.class);
		command.finishMatch(id, new Score(1, 0), oneHourAfter);

		// Then expect error
	}
}
