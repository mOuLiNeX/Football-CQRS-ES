package fr.manu.cqrs.repository;

import java.util.Map;

import com.google.common.collect.Maps;

import fr.manu.cqrs.domain.Match;
import fr.manu.cqrs.domain.MatchId;

public class MatchRepository {
    private final Map<MatchId, Match> memDB = Maps.newHashMap();

    public Match find(MatchId id) {
        return memDB.get(id);
    }

    public void save(Match match) {
        memDB.put(match.id, match);
    }

}
