package fr.manu.cqrs.service;

import java.time.LocalDateTime;

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

    public void startMatch(MatchId id, LocalDateTime matchLocalDateTime) throws MatchAlreadyStartedException {
        Match match = getMatch(id);
        match.startMatch(matchLocalDateTime);
    }

    public void finishMatch(MatchId id, Score score, LocalDateTime endMatchLocalDateTime) throws MatchNotStartedException {
        Match match = getMatch(id);
		match.finishWithScore(score, endMatchLocalDateTime);
    }

    private Match getMatch(MatchId id) {
        return matchRepository.find(id);
    }
}
