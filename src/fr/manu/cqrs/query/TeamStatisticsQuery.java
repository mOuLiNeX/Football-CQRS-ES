package fr.manu.cqrs.query;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

// TODO A bouger (code infra)
public class TeamStatisticsQuery {
    private static final Map<String, TeamState> states = Maps.newHashMap();

    public static Collection<TeamState> findAll() {
        return states.values();
    }

    public static TeamState findByName(String teamName) {
        return states.get(teamName);
    }

    static void addStats(TeamState... stat) {
        for (TeamState teamState : stat) {
            states.put(teamState.name, teamState);
        }
    }

    public static void init() {
        states.clear();
    }

}
