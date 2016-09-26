package fr.manu.cqrs.query;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.Subscribe;

import fr.manu.cqrs.domain.MatchId;
import fr.manu.cqrs.events.MatchCreatedEvent;
import fr.manu.cqrs.events.MatchFinishedEvent;

@Singleton
public class TeamStateHandler {
	@Inject
	TeamStatisticsQuery query;

	Map<MatchId, Map<String, TeamState>> tempTable;

	public TeamStateHandler() {
		tempTable = new HashMap<>();
	}

	@Subscribe
	public void handle(MatchFinishedEvent event) {
		Map<String, TeamState> teams = (Map<String, TeamState>) tempTable.get(event.matchId);
		// Convention sur l'ordre de declaration des equipes
		TeamState home = teams.get("home");
		TeamState away = teams.get("away");
		if (event.homeGoals > event.awayGoals) {
			home.addVictory();
			away.addDefeat();
		} else {
			home.addDefeat();
			away.addVictory();
		}
		query.addStats(home, away);
		// Clean-up
		tempTable.remove(event.matchId, home);
		tempTable.remove(event.matchId, away);
	}

	@Subscribe
	public void handle(MatchCreatedEvent event) {
		tempTable.put(event.matchId,
				ImmutableMap.of("home", new TeamState(event.homeTeam), "away", new TeamState(event.awayTeam)));
	}
}
