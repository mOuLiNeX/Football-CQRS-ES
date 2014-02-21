package fr.manu.cqrs.query;

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

public class TeamStatisticsQueryTest {
    @Before
    public void setUp() {
        EventSourcingHelperTest.init();
    }

    @Test
    public void testFinishingMatchProduceStats() {
        final MatchId id = MatchId.newMatchId();
        final String victoriousTeam = "team1";
        final String defeatedTeam = "team2";
        // Given events
        EventSourcingHelperTest.givenEvents(
            new MatchCreatedEvent(id, victoriousTeam, defeatedTeam),
            new MatchStartedEvent(id, new Date()),
            new MatchFinishedEvent(id, new Date(), 3, 0));

        // When query
        QueryService srv = new QueryService();
        Collection<TeamState> allTeams = srv.getRanking();

        // Then expect states
        assertFalse(allTeams.isEmpty());
        assertTrue(Iterables.contains(allTeams, TeamState.createVictoryStat(victoriousTeam)));
        assertTrue(Iterables.contains(allTeams, TeamState.createDefeatStat(defeatedTeam)));
    }

    @Test
    public void testNonFinishedMatchDoesntProduceStats() {
        final MatchId id = MatchId.newMatchId();
        // Given events
        EventSourcingHelperTest.givenEvents(
            new MatchCreatedEvent(id, "team1", "team2"),
            new MatchStartedEvent(id, new Date()));

        // When query
        QueryService srv = new QueryService();
        Collection<TeamState> allTeams = srv.getRanking();

        // Then expect states
        assertTrue(allTeams.isEmpty());
    }

    @Test
    public void testFinishingMatchesProduceMoreStats() {
        final MatchId id1 = MatchId.newMatchId();
        final MatchId id2 = MatchId.newMatchId();
        final String victoriousTeamMatch1 = "team1";
        final String defeatedTeamMatch1 = "team2";
        final String victoriousTeamMatch2 = "team3";
        final String defeatedTeamMatch2 = "team4";

        // Given events
        EventSourcingHelperTest.givenEvents(
            new MatchCreatedEvent(id1, victoriousTeamMatch1, defeatedTeamMatch1),
            new MatchStartedEvent(id1, new Date()),
            new MatchCreatedEvent(id2, defeatedTeamMatch2, victoriousTeamMatch2),
            new MatchStartedEvent(id2, new Date()),
            new MatchFinishedEvent(id2, new Date(), 2, 3),
            new MatchFinishedEvent(id1, new Date(), 3, 0));

        // When query
        QueryService srv = new QueryService();
        Collection<TeamState> allTeams = srv.getRanking();

        // Then expect states
        assertTrue(Iterables.contains(allTeams, TeamState.createVictoryStat(victoriousTeamMatch1)));
        assertTrue(Iterables.contains(allTeams, TeamState.createDefeatStat(defeatedTeamMatch1)));
        assertTrue(Iterables.contains(allTeams, TeamState.createVictoryStat(victoriousTeamMatch2)));
        assertTrue(Iterables.contains(allTeams, TeamState.createDefeatStat(defeatedTeamMatch2)));
    }
}
