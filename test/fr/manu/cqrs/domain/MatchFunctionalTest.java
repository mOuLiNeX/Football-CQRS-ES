package fr.manu.cqrs.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterables;

import fr.manu.cqrs.domain.event.MatchCreatedEvent;
import fr.manu.cqrs.domain.event.MatchEvent;
import fr.manu.cqrs.domain.event.MatchEventStore;
import fr.manu.cqrs.domain.event.MatchFinishedEvent;
import fr.manu.cqrs.domain.event.MatchStartedEvent;
import fr.manu.cqrs.exception.MatchAlreadyStartedException;
import fr.manu.cqrs.exception.MatchNotStartedException;
import fr.manu.cqrs.repository.MatchRepository;
import fr.manu.cqrs.service.CommandService;

public class MatchFunctionalTest {
    private void givenEvents(MatchEvent... events) {
        MatchEventStore.append(events);
    }

    private void expectEvent(MatchEvent expectedEvt) {
        Collection<MatchEvent> evts = MatchEventStore.getEvents(expectedEvt.getId());
        assertNotNull(evts);
        assertTrue(!evts.isEmpty());
        MatchEvent actualEvt = Iterables.getLast(evts);
        assertEquals(expectedEvt, actualEvt);

    }

    @Before
    public void setUp() {
        MatchEventStore.init();
    }

    @Test
    public void testCanDefineMatchWithTwoTeams() {
        String home = "team1";
        String away = "team2";

        // When command
        CommandService srv = new CommandService(new MatchRepository());
        MatchId id = srv.createMatch(home, away);

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
        CommandService srv = new CommandService(new MatchRepository());
        srv.startMatch(id, startMatchDate);

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
        givenEvents(new MatchCreatedEvent(id, "team1", "team2"),
            new MatchStartedEvent(id, startMatchDate));

        // When command
        CommandService srv = new CommandService(new MatchRepository());
        srv.finishMatch(id, score, endMatchDate);

        // Then expect event
        expectEvent(new MatchFinishedEvent(id, endMatchDate, score.homeGoals, score.awayGoals));
    }

    @Test(expected = MatchNotStartedException.class)
    public void testCannotFinishAMatchBeforeItStarts() {
        MatchId id = MatchId.newMatchId();
        Date endMatchDate = new Date();
        // Given events
        givenEvents(new MatchCreatedEvent(id, "team1", "team2"),
            new MatchStartedEvent(id, new Date()));

        // When command
        CommandService srv = new CommandService(new MatchRepository());
        srv.finishMatch(id, new Score(1, 0), endMatchDate);

        // Then expect error
    }
}
