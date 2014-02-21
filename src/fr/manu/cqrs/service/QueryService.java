package fr.manu.cqrs.service;

import java.util.Collection;

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

}
