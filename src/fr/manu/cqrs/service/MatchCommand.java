package fr.manu.cqrs.service;

import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.inject.Singleton;

import fr.manu.cqrs.domain.Match;
import fr.manu.cqrs.domain.MatchId;
import fr.manu.cqrs.domain.Score;
import fr.manu.cqrs.domain.exception.MatchAlreadyFinishedException;
import fr.manu.cqrs.domain.exception.MatchAlreadyStartedException;
import fr.manu.cqrs.domain.exception.MatchNotStartedException;
import fr.manu.cqrs.events.MatchCreatedEvent;
import fr.manu.cqrs.events.MatchEvent;
import fr.manu.cqrs.events.MatchEventBus;
import fr.manu.cqrs.events.MatchFinishedEvent;
import fr.manu.cqrs.events.MatchStartedEvent;
import fr.manu.cqrs.repository.MatchRepository;

@Singleton
public class MatchCommand {

	@Inject
	MatchRepository matchRepository;

	@Inject
	MatchEventBus evtBus;

	private void publishEvent(MatchEvent event) {
		evtBus.publishEvent(event);
	}

	public MatchId createMatch(String home, String away) {
		MatchId id = MatchId.newMatchId();
		new Match(id, home, away);
		publishEvent(new MatchCreatedEvent(id, home, away));
		return id;
	}

	public void startMatch(MatchId id, LocalDateTime matchDate) throws MatchAlreadyStartedException {
		Match match = getMatch(id);
		match.startMatch(matchDate);
		publishEvent(new MatchStartedEvent(match.id, matchDate));
	}

	public void finishMatch(MatchId id, Score score, LocalDateTime endMatch)
			throws MatchNotStartedException, MatchAlreadyFinishedException {
		Match match = getMatch(id);
		match.finishWithScore(score, endMatch);
		publishEvent(new MatchFinishedEvent(match.id, endMatch, score.homeGoals, score.awayGoals));
	}

	private Match getMatch(MatchId id) {
		return matchRepository.find(id);
	}
}
