package fr.manu.cqrs.domain.event;

import java.util.Objects;

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
        return Objects.equals(matchId, other.matchId) && Objects.equals(homeTeam, other.homeTeam)
            && Objects.equals(awayTeam, other.awayTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchId, homeTeam, awayTeam);
    }
}
