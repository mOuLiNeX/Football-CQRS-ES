package fr.manu.cqrs.domain;

import static fr.manu.cqrs.EventSourcingAsserter.expectEvent;
import static fr.manu.cqrs.EventSourcingAsserter.givenEvents;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import fr.manu.cqrs.EventSourcingtTestRule;
import fr.manu.cqrs.domain.event.MatchCreatedEvent;
import fr.manu.cqrs.domain.event.MatchFinishedEvent;
import fr.manu.cqrs.domain.event.MatchStartedEvent;
import fr.manu.cqrs.exception.MatchAlreadyStartedException;
import fr.manu.cqrs.exception.MatchNotStartedException;
import fr.manu.cqrs.repository.MatchRepository;
import fr.manu.cqrs.service.CommandService;

public class MatchFunctionalTest {
	@Rule
	public EventSourcingtTestRule defaults = new EventSourcingtTestRule();
	private CommandService command;

	@Before
	public void setUp() {
		command = new CommandService(new MatchRepository());
	}
	
	@Test
	public void testCanDefineMatchWithTwoTeams() {
		String home = "team1";
		String away = "team2";

		// When command
		MatchId id = command.createMatch(home, away);

		// Then expect event
		expectEvent(new MatchCreatedEvent(id, home, away));
	}

	@Test
	public void testCanStartMatchAtGivenTime() {
		MatchId id = MatchId.newMatchId();
		Date startMatchDate = new Date();

		// Given events
		givenEvents(new MatchCreatedEvent(id, "team1", "team2"));

		// When command
		command.startMatch(id, startMatchDate);

		// Then expect event
		expectEvent(new MatchStartedEvent(id, startMatchDate));
	}

	@Test(expected = MatchAlreadyStartedException.class)
	public void testCannotStartMatchTwice() {
		MatchId id = MatchId.newMatchId();
		Date startMatchDate = new Date();

		// Given events
		givenEvents(new MatchCreatedEvent(id, "team1", "team2"),
				new MatchStartedEvent(id, startMatchDate));

		// When command
		command.startMatch(id, startMatchDate);

		// Then expect error
	}

	@Test
	public void testCanFinishAMatchWithScore() {
		MatchId id = MatchId.newMatchId();
		Date startMatchDate = new Date();
		Date endMatchDate = new Date();
		Score score = new Score(1, 0);

		// Given events
		givenEvents(new MatchCreatedEvent(id, "team1", "team2"),
				new MatchStartedEvent(id, startMatchDate));

		// When command
		command.finishMatch(id, score, endMatchDate);

		// Then expect event
		expectEvent(new MatchFinishedEvent(id, endMatchDate, score.homeGoals,
				score.awayGoals));
	}

	@Test(expected = MatchNotStartedException.class)
	public void testCannotFinishAMatchBeforeItStarts() {
		MatchId id = MatchId.newMatchId();
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.HOUR, -1);
		Date oneHourBefore = cal.getTime();

		// Given events
		givenEvents(new MatchCreatedEvent(id, "team1", "team2"),
				new MatchStartedEvent(id, new Date()));

		// When command
		command.finishMatch(id, new Score(1, 0), oneHourBefore);

		// Then expect error
	}
}
