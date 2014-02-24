package fr.manu.cqrs.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterables;

import fr.manu.cqrs.EventSourcingHelperTest;
import fr.manu.cqrs.domain.MatchId;
import fr.manu.cqrs.domain.event.MatchCreatedEvent;
import fr.manu.cqrs.domain.event.MatchFinishedEvent;
import fr.manu.cqrs.domain.event.MatchStartedEvent;
import fr.manu.cqrs.service.QueryService;

public class MatchQueryTest {
    @Before
    public void setUp() {
        EventSourcingHelperTest.init();
    }

    @Test
    public void testQueryPlannedMatch() {
        final MatchId id = MatchId.newMatchId();
        // Given events
        EventSourcingHelperTest.givenEvents(
            new MatchCreatedEvent(id, "team1", "team2"));

        // When query
        QueryService srv = new QueryService();
        Collection<MatchState> allMatches = srv.getCurrentMatches();

        // Then expect states
        assertFalse(allMatches.isEmpty());
        assertTrue(Iterables.contains(allMatches, MatchState.createPlannedMatch(id)));
    }

    @Test
    public void testQueryStartedMatch() {
        final MatchId id = MatchId.newMatchId();
        // Given events
        EventSourcingHelperTest.givenEvents(
            new MatchCreatedEvent(id, "team1", "team2"),
            new MatchStartedEvent(id, new Date()));

        // When query
        QueryService srv = new QueryService();
        Collection<MatchState> allMatches = srv.getCurrentMatches();

        // Then expect states
        assertFalse(allMatches.isEmpty());
        assertTrue(Iterables.contains(allMatches, MatchState.createStartedMatch(id)));
    }

    @Test
    public void testQueryFinishedMatch() {
        final MatchId id = MatchId.newMatchId();
        // Given events
        EventSourcingHelperTest.givenEvents(
            new MatchCreatedEvent(id, "team1", "team2"),
            new MatchStartedEvent(id, new Date()),
            new MatchFinishedEvent(id, new Date(), 3, 0));

        // When query
        QueryService srv = new QueryService();
        Collection<MatchState> allMatches = srv.getCurrentMatches();

        // Then expect states
        assertTrue(allMatches.isEmpty());
    }

    @Test
    public void testQueryByMatch() {
        final MatchId id1 = MatchId.newMatchId();
        final MatchId id2 = MatchId.newMatchId();
        // Given events
        EventSourcingHelperTest.givenEvents(
            new MatchCreatedEvent(id1, "team1", "team2"),
            new MatchStartedEvent(id1, new Date()),
            new MatchCreatedEvent(id2, "team4", "team3"));

        // When query
        QueryService srv = new QueryService();

        // Then expect states
        assertEquals(MatchState.createStartedMatch(id1), srv.getCurrentMatchById(id1));
        assertEquals(MatchState.createPlannedMatch(id2), srv.getCurrentMatchById(id2));
    }
}
