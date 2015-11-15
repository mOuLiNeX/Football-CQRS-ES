package fr.manu.cqrs.events;

import java.time.LocalDateTime;
import java.util.Objects;

import fr.manu.cqrs.domain.Match;
import fr.manu.cqrs.domain.MatchId;
import fr.manu.cqrs.domain.Score;

public class MatchFinishedEvent extends MatchEvent {

	public final LocalDateTime endMatchDate;

	public final int homeGoals;
	public final int awayGoals;

	public MatchFinishedEvent(MatchId matchId, LocalDateTime endMatchDate, int homeGoals, int awayGoals) {
		super(matchId);
		this.endMatchDate = endMatchDate;
		this.homeGoals = homeGoals;
		this.awayGoals = awayGoals;
	}

	@Override
	public Match applyOn(Match match) {
		match.finishWithScore(new Score(homeGoals, awayGoals), endMatchDate);
		return match;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MatchFinishedEvent other = (MatchFinishedEvent) obj;
		return Objects.equals(matchId, other.matchId) && Objects.equals(endMatchDate, other.endMatchDate)
				&& Objects.equals(homeGoals, other.homeGoals) && Objects.equals(awayGoals, other.awayGoals);
	}

	@Override
	public int hashCode() {
		return Objects.hash(matchId, endMatchDate, homeGoals, awayGoals);
	}
}
