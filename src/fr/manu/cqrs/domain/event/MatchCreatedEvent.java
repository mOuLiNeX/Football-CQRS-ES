package fr.manu.cqrs.domain.event;

import fr.manu.cqrs.domain.MatchId;

public class MatchCreatedEvent implements MatchEvent {
    public final MatchId matchId;

    public final String matchTeamName1;

    public final String matchTeamName2;

    public MatchCreatedEvent(MatchId matchId, String matchTeamName1, String matchTeamName2) {
        super();
        this.matchId = matchId;
        this.matchTeamName1 = matchTeamName1;
        this.matchTeamName2 = matchTeamName2;
    }

    @Override
    public MatchId getId() {
        return matchId;
    }
}
