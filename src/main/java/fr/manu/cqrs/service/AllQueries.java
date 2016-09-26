package fr.manu.cqrs.service;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import fr.manu.cqrs.domain.MatchId;
import fr.manu.cqrs.query.MatchQuery;
import fr.manu.cqrs.query.MatchState;
import fr.manu.cqrs.query.TeamState;
import fr.manu.cqrs.query.TeamStatisticsQuery;

@Singleton
public class AllQueries {

	@Inject
	TeamStatisticsQuery teamStatistics;
	@Inject
	MatchQuery match;

	public Collection<TeamState> getRanking() {
		return teamStatistics.findAll();
	}

	public TeamState getTeamStatistics(String teamName) {
		return teamStatistics.findByName(teamName);
	}

	public Collection<MatchState> getCurrentMatches() {
		return match.findAll();
	}

	public MatchState getCurrentMatchById(MatchId id) {
		return match.findById(id);
	}

}
