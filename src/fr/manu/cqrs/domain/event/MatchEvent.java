package fr.manu.cqrs.domain.event;

import java.time.Instant;
import java.util.UUID;

import fr.manu.cqrs.domain.Match;
import fr.manu.cqrs.domain.MatchId;

// TODO A bouger (code marqueur)
public abstract class MatchEvent implements Comparable<MatchEvent> {
	public final MatchId matchId;
	public final Instant recordTime;
	public final UUID id;

	public MatchEvent(MatchId matchId) {
		super();
		this.matchId = matchId;
		recordTime = Instant.now();
		id = UUID.randomUUID();
	}

	public abstract void applyOn(Match match);

	@Override
	public int compareTo(MatchEvent other) {
		return this.recordTime.compareTo(other.recordTime);
	}
}
