package fr.manu.cqrs.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import fr.manu.cqrs.EventSourcingHelperTest;
import fr.manu.cqrs.domain.event.MatchCreatedEvent;
import fr.manu.cqrs.domain.event.MatchFinishedEvent;
import fr.manu.cqrs.domain.event.MatchStartedEvent;
import fr.manu.cqrs.exception.MatchAlreadyStartedException;
import fr.manu.cqrs.exception.MatchNotStartedException;
import fr.manu.cqrs.repository.MatchRepository;
import fr.manu.cqrs.service.CommandService;

public class MatchFunctionalTest {
    @Before
    public void setUp() {
        EventSourcingHelperTest.init();
    }

    @Test
    public void testCanDefineMatchWithTwoTeams() {
        String home = "team1";
        String away = "team2";

        // When command
        CommandService srv = new CommandService(new MatchRepository());
        MatchId id = srv.createMatch(home, away);

        // Then expect event
        EventSourcingHelperTest.expectEvent(new MatchCreatedEvent(id, home, away));
    }

    @Test
    public void testCanStartMatchAtGivenTime() {
        MatchId id = MatchId.newMatchId();
        Date startMatchDate = new Date();

        // Given events
        EventSourcingHelperTest.givenEvents(new MatchCreatedEvent(id, "team1", "team2"));

        // When command
        CommandService srv = new CommandService(new MatchRepository());
        srv.startMatch(id, startMatchDate);

        // Then expect event
        EventSourcingHelperTest.expectEvent(new MatchStartedEvent(id, startMatchDate));
    }

    @Test(expected = MatchAlreadyStartedException.class)
    public void testCannotStartMatchTwice() {
        MatchId id = MatchId.newMatchId();
        Date startMatchDate = new Date();
        // Given events
        EventSourcingHelperTest.givenEvents(new MatchCreatedEvent(id, "team1", "team2"),
            new MatchStartedEvent(id, startMatchDate));

        // When command
        CommandService srv = new CommandService(new MatchRepository());
        srv.startMatch(id, startMatchDate);

        // Then expect error
    }

    @Test
    public void testCanFinishAMatchWithScore() {
        MatchId id = MatchId.newMatchId();
        Date startMatchDate = new Date();
        Date endMatchDate = new Date();
        Score score = new Score(1, 0);

        // Given events
        EventSourcingHelperTest.givenEvents(
            new MatchCreatedEvent(id, "team1", "team2"),
            new MatchStartedEvent(id, startMatchDate));

        // When command
        CommandService srv = new CommandService(new MatchRepository());
        srv.finishMatch(id, score, endMatchDate);

        // Then expect event
        EventSourcingHelperTest.expectEvent(
            new MatchFinishedEvent(id, endMatchDate, score.homeGoals, score.awayGoals));
    }

    @Test(expected = MatchNotStartedException.class)
    public void testCannotFinishAMatchBeforeItStarts() {
        MatchId id = MatchId.newMatchId();
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.HOUR, -1);
        Date oneHourBefore = cal.getTime();

        // Given events
        EventSourcingHelperTest.givenEvents(
            new MatchCreatedEvent(id, "team1", "team2"),
            new MatchStartedEvent(id, new Date()));

        // When command
        CommandService srv = new CommandService(new MatchRepository());
        srv.finishMatch(id, new Score(1, 0), oneHourBefore);

        // Then expect error
    }
}
