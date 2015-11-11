package fr.manu.cqrs.query;

import java.util.List;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.Subscribe;

import fr.manu.cqrs.domain.MatchId;
import fr.manu.cqrs.domain.event.MatchCreatedEvent;
import fr.manu.cqrs.domain.event.MatchFinishedEvent;

public class TeamStateHandler {

    Multimap<MatchId, TeamState> tempTable;

    public TeamStateHandler() {
        tempTable = LinkedListMultimap.create();
    }

    @SuppressWarnings("unchecked")
    @Subscribe
    public void handle(MatchFinishedEvent event) {
        List<TeamState> teams = (List) tempTable.get(event.getId());
		// Convention sur l'ordre de declaration des equipes
        TeamState home = teams.get(0);
        TeamState away = teams.get(1);
        if (event.homeGoals > event.awayGoals) {
            home.addVictory();
            away.addDefeat();
        } else {
            home.addDefeat();
            away.addVictory();
        }
        TeamStatisticsQuery.addStats(home, away);
        // Clean-up
        tempTable.remove(event.getId(), home);
        tempTable.remove(event.getId(), away);
    }

    @Subscribe
    public void handle(MatchCreatedEvent event) {
        tempTable.put(event.getId(), new TeamState(event.homeTeam));
        tempTable.put(event.getId(), new TeamState(event.awayTeam));
    }
}
