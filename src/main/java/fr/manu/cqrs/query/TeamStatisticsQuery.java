package fr.manu.cqrs.query;

import java.util.Collection;
import java.util.Map;

import javax.inject.Singleton;

import com.google.common.collect.Maps;

@Singleton
public class TeamStatisticsQuery {
	private final Map<String, TeamState> states = Maps.newHashMap();

	public Collection<TeamState> findAll() {
        return states.values();
    }

	public TeamState findByName(String teamName) {
        return states.get(teamName);
    }

	void addStats(TeamState... stat) {
        for (TeamState teamState : stat) {
            states.put(teamState.name, teamState);
        }
    }

	public void init() {
        states.clear();
    }

}
