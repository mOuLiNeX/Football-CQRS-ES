package fr.manu.cqrs.events;

import java.time.LocalDateTime;
import java.util.Objects;

import fr.manu.cqrs.domain.Match;
import fr.manu.cqrs.domain.MatchId;

public class MatchStartedEvent extends MatchEvent {
	public final LocalDateTime matchDate;

	public MatchStartedEvent(MatchId matchId, LocalDateTime matchDate) {
		super(matchId);
		this.matchDate = matchDate;
	}

	@Override
	public Match applyOn(Match match) {
		match.startMatch(this.matchDate);
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
		MatchStartedEvent other = (MatchStartedEvent) obj;
		return Objects.equals(matchId, other.matchId) && Objects.equals(matchDate, other.matchDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(matchId, matchDate);
	}

}
