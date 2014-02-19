package fr.manu.cqrs.service;

import java.util.Date;

import fr.manu.cqrs.domain.Match;
import fr.manu.cqrs.domain.MatchId;
import fr.manu.cqrs.domain.Score;
import fr.manu.cqrs.exception.MatchAlreadyStartedException;
import fr.manu.cqrs.exception.MatchNotStartedException;
import fr.manu.cqrs.repository.MatchRepository;

public class CommandService {

    private final MatchRepository matchRepository;

    public CommandService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public MatchId createMatch(String home, String away) {
        MatchId id = MatchId.newMatchId();
        new Match(id, home, away);
        return id;
    }

    public void startMatch(MatchId id, Date matchDate) throws MatchAlreadyStartedException {
        Match match = getMatch(id);
        match.startMatch(matchDate);
    }

    public void finishMatch(MatchId id, Score score, Date endMatchDate) throws MatchNotStartedException {
        Match match = getMatch(id);
        match.finishWithScore(score, endMatchDate);
    }

    private Match getMatch(MatchId id) {
        return matchRepository.find(id);
    }
}
