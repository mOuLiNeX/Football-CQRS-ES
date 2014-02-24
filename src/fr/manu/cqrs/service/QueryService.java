package fr.manu.cqrs.service;

import java.util.Collection;

import fr.manu.cqrs.domain.MatchId;
import fr.manu.cqrs.query.MatchQuery;
import fr.manu.cqrs.query.MatchState;
import fr.manu.cqrs.query.TeamState;
import fr.manu.cqrs.query.TeamStatisticsQuery;

public class QueryService {

    public QueryService() {
    }

    public Collection<TeamState> getRanking() {
        return TeamStatisticsQuery.findAll();
    }

    public TeamState getTeamStatistics(String teamName) {
        return TeamStatisticsQuery.findByName(teamName);
    }

    public Collection<MatchState> getCurrentMatches() {
        return MatchQuery.findAll();
    }

    public MatchState getCurrentMatchById(MatchId id) {
        return MatchQuery.findById(id);
    }

}
