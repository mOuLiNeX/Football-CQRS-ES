package fr.manu.cqrs.domain.event;

import java.time.LocalDateTime;
import java.util.Objects;

import fr.manu.cqrs.domain.MatchId;

public class MatchFinishedEvent implements MatchEvent {
    private final MatchId matchId;

    public final LocalDateTime matchLocalDateTime;

    public final int homeGoals;
    public final int awayGoals;

    public MatchFinishedEvent(MatchId matchId, LocalDateTime matchLocalDateTime, int homeGoals, int awayGoals) {
        super();
        this.matchId = matchId;
        this.matchLocalDateTime = matchLocalDateTime;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
    }

    @Override
    public MatchId getId() {
        return matchId;
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
        return Objects.equals(matchId, other.matchId) && Objects.equals(matchLocalDateTime, other.matchLocalDateTime)
            && Objects.equals(homeGoals, other.homeGoals) && Objects.equals(awayGoals, other.awayGoals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchId, matchLocalDateTime, homeGoals, awayGoals);
    }
}
