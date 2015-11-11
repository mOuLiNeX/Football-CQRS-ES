package fr.manu.cqrs.domain;

import java.time.LocalDateTime;

import fr.manu.cqrs.domain.event.MatchCreatedEvent;
import fr.manu.cqrs.domain.event.MatchEvent;
import fr.manu.cqrs.domain.event.MatchEventBus;
import fr.manu.cqrs.domain.event.MatchFinishedEvent;
import fr.manu.cqrs.domain.event.MatchStartedEvent;
import fr.manu.cqrs.exception.MatchAlreadyStartedException;
import fr.manu.cqrs.exception.MatchNotStartedException;

public class Match {
    public final MatchId id;

	public LocalDateTime matchDate;

	public Team teams[] = new Team[2];

	public boolean finished;

    private void publishEvent(MatchEvent event) {
        MatchEventBus.publishEvent(event);
    }

	public Match(MatchId id) {
        this.id = id;
    }

    public Match(MatchId id, String home, String away) {
        this(id);
        publishEvent(new MatchCreatedEvent(id, home, away));
    }

    public void startMatch(LocalDateTime matchDate) throws MatchAlreadyStartedException {
        if (this.matchDate != null) {
            throw new MatchAlreadyStartedException("Cannot start an already started match");
        }
        publishEvent(new MatchStartedEvent(this.id, matchDate));
    }

    public void finishWithScore(Score score, LocalDateTime endMatchDate) throws MatchNotStartedException {
        if (this.matchDate == null)
            System.out.print("the match has not started");
        if (!finished) {
            System.out.print("Finish the match");
            if (endMatchDate.isBefore(this.matchDate)) {
                throw new MatchNotStartedException("Could not finish a non started match");
            }
            publishEvent(new MatchFinishedEvent(this.id, endMatchDate, score.homeGoals, score.awayGoals));
        }
    }

    public String getHomeTeamName() {
        return teams[0].name;
    }

    public String getAwayTeamName() {
        return teams[1].name;
    }

    public boolean isFinished() {
        return finished;
    }
}
