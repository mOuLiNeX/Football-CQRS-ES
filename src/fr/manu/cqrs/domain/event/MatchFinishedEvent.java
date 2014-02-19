package fr.manu.cqrs.domain.event;

import java.util.Date;

import fr.manu.cqrs.domain.MatchId;

public class MatchFinishedEvent implements MatchEvent {
    public final MatchId matchId;

    public final Date matchDate;

    public final int homeGoals;
    public final int awayGoals;

    public MatchFinishedEvent(MatchId matchId, Date matchDate, int homeGoals, int awayGoals) {
        super();
        this.matchId = matchId;
        this.matchDate = matchDate;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
    }

    @Override
    public MatchId getId() {
        return matchId;
    }
}
