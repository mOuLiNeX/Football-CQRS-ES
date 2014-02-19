package fr.manu.cqrs.domain.event;

import com.google.common.base.Objects;

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
        return Objects.equal(matchId, other.matchId) && Objects.equal(matchTeamName1, other.matchTeamName1)
            && Objects.equal(matchTeamName2, other.matchTeamName2);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(matchId, matchTeamName1, matchTeamName2);
    }
}
