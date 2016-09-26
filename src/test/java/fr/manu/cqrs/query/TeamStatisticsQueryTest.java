package fr.manu.cqrs.query;

import java.time.LocalDateTime;
import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import fr.manu.cqrs.EventSourcingtTestRule;
import fr.manu.cqrs.IoCInjectorRule;
import fr.manu.cqrs.domain.MatchId;
import fr.manu.cqrs.events.MatchCreatedEvent;
import fr.manu.cqrs.events.MatchFinishedEvent;
import fr.manu.cqrs.events.MatchStartedEvent;
import fr.manu.cqrs.service.AllQueries;

public class TeamStatisticsQueryTest {
	@ClassRule
	public static final IoCInjectorRule INJECT = new IoCInjectorRule();

	@Rule
	public EventSourcingtTestRule eventSrcContext = new EventSourcingtTestRule(INJECT);

	@Test
	public void testFinishingMatchProduceStats() {
		final MatchId id = MatchId.newMatchId();
		final String victoriousTeam = "team1";
		final String defeatedTeam = "team2";
		// Given events
		eventSrcContext.givenEvents(new MatchCreatedEvent(id, victoriousTeam, defeatedTeam),
				new MatchStartedEvent(id, LocalDateTime.now()), new MatchFinishedEvent(id, LocalDateTime.now(), 3, 0));

		// When query
		AllQueries query = INJECT.getInstance(AllQueries.class);
		Collection<TeamState> allTeams = query.getRanking();

		// Then expect states
		Assertions.assertThat(allTeams).isNotEmpty().contains(TeamState.createVictoryStat(victoriousTeam),
				TeamState.createDefeatStat(defeatedTeam));
	}

	@Test
	public void testNonFinishedMatchDoesntProduceStats() {
		final MatchId id = MatchId.newMatchId();
		// Given events
		eventSrcContext.givenEvents(new MatchCreatedEvent(id, "team1", "team2"),
				new MatchStartedEvent(id, LocalDateTime.now()));

		// When query
		AllQueries query = INJECT.getInstance(AllQueries.class);
		Collection<TeamState> allTeams = query.getRanking();

		// Then expect states
		Assertions.assertThat(allTeams).isEmpty();
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
		eventSrcContext.givenEvents(new MatchCreatedEvent(id1, victoriousTeamMatch1, defeatedTeamMatch1),
				new MatchStartedEvent(id1, LocalDateTime.now()),
				new MatchCreatedEvent(id2, defeatedTeamMatch2, victoriousTeamMatch2),
				new MatchStartedEvent(id2, LocalDateTime.now()), new MatchFinishedEvent(id2, LocalDateTime.now(), 2, 3),
				new MatchFinishedEvent(id1, LocalDateTime.now(), 3, 0));

		// When query
		AllQueries query = INJECT.getInstance(AllQueries.class);
		Collection<TeamState> allTeams = query.getRanking();

		// Then expect states
		Assertions.assertThat(allTeams).contains(TeamState.createVictoryStat(victoriousTeamMatch1),
				TeamState.createDefeatStat(defeatedTeamMatch1), TeamState.createVictoryStat(victoriousTeamMatch2),
				TeamState.createDefeatStat(defeatedTeamMatch2));
	}

	@Test
	public void testStatsPerTeam() {
		final MatchId id1 = MatchId.newMatchId();
		final MatchId id2 = MatchId.newMatchId();
		final String victoriousTeamMatch1 = "team1";
		final String defeatedTeamMatch1 = "team2";
		final String victoriousTeamMatch2 = "team3";
		final String defeatedTeamMatch2 = "team4";

		// Given events
		eventSrcContext.givenEvents(new MatchCreatedEvent(id1, victoriousTeamMatch1, defeatedTeamMatch1),
				new MatchStartedEvent(id1, LocalDateTime.now()),
				new MatchCreatedEvent(id2, defeatedTeamMatch2, victoriousTeamMatch2),
				new MatchStartedEvent(id2, LocalDateTime.now()), new MatchFinishedEvent(id2, LocalDateTime.now(), 2, 3),
				new MatchFinishedEvent(id1, LocalDateTime.now(), 3, 0));

		// Then expect states
		AllQueries query = INJECT.getInstance(AllQueries.class);

		Assertions.assertThat(query.getTeamStatistics(victoriousTeamMatch1))
				.isEqualTo(TeamState.createVictoryStat(victoriousTeamMatch1));
		Assertions.assertThat(query.getTeamStatistics(victoriousTeamMatch2))
				.isEqualTo(TeamState.createVictoryStat(victoriousTeamMatch2));
		Assertions.assertThat(query.getTeamStatistics(defeatedTeamMatch1))
				.isEqualTo(TeamState.createDefeatStat(defeatedTeamMatch1));
		Assertions.assertThat(query.getTeamStatistics(defeatedTeamMatch2))
				.isEqualTo(TeamState.createDefeatStat(defeatedTeamMatch2));
	}
}
