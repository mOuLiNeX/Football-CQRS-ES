package fr.manu.cqrs.domain.event;

import com.google.common.base.Objects;

import fr.manu.cqrs.domain.MatchId;

public class MatchCreatedEvent implements MatchEvent {
    private final MatchId matchId;

    public final String homeTeam;

    public final String awayTeam;

    public MatchCreatedEvent(MatchId matchId, String matchTeamName1, String matchTeamName2) {
        super();
        this.matchId = matchId;
        this.homeTeam = matchTeamName1;
        this.awayTeam = matchTeamName2;
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
        MatchCreatedEvent other = (MatchCreatedEvent) obj;
        return Objects.equal(matchId, other.matchId) && Objects.equal(homeTeam, other.homeTeam)
            && Objects.equal(awayTeam, other.awayTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(matchId, homeTeam, awayTeam);
    }
}
