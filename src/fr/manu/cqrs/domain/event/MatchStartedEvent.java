package fr.manu.cqrs.domain.event;

import java.util.Date;

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
}
