package fr.manu.cqrs.domain.event;

import java.util.Date;

import com.google.common.base.Objects;

import fr.manu.cqrs.domain.MatchId;

public class MatchStartedEvent implements MatchEvent {
    public final MatchId matchId;

    public final Date matchDate;

    public MatchStartedEvent(MatchId matchId, Date matchDate) {
        super();
        this.matchId = matchId;
        this.matchDate = matchDate;
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
        MatchStartedEvent other = (MatchStartedEvent) obj;
        return Objects.equal(matchId, other.matchId) && Objects.equal(matchDate, other.matchDate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(matchId, matchDate);
    }
}
