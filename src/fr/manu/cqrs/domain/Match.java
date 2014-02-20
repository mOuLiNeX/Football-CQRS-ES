package fr.manu.cqrs.domain;

import java.util.Date;

import fr.manu.cqrs.domain.event.MatchCreatedEvent;
import fr.manu.cqrs.domain.event.MatchEvent;
import fr.manu.cqrs.domain.event.MatchEventBus;
import fr.manu.cqrs.domain.event.MatchFinishedEvent;
import fr.manu.cqrs.domain.event.MatchStartedEvent;
import fr.manu.cqrs.exception.MatchAlreadyStartedException;
import fr.manu.cqrs.exception.MatchNotStartedException;

public class Match {
    public final MatchId id;

    private Date matchDate;

    private Team teams[] = new Team[2];

    private boolean finished;

    private void publishEvent(MatchEvent event) {
        MatchEventBus.publishEvent(event);
    }

    // TODO A déplacer + faire mieux (pattern Visitor ?) mais c'est pour l'exemple
    public void handle(MatchEvent evt) {
        if (MatchCreatedEvent.class.isAssignableFrom(evt.getClass())) {
            this.handleCreation((MatchCreatedEvent) evt);
        } else if (MatchFinishedEvent.class.isAssignableFrom(evt.getClass())) {
            this.handleFinish((MatchFinishedEvent) evt);
        } else if (MatchStartedEvent.class.isAssignableFrom(evt.getClass())) {
            this.handleStart((MatchStartedEvent) evt);
        } else {
            throw new RuntimeException("Cannot handle event " + evt + " in Match instance");
        }
    }

    private void handleCreation(MatchCreatedEvent matchCreatedEvent) {
        this.teams[0] = new Team(matchCreatedEvent.homeTeam);
        this.teams[1] = new Team(matchCreatedEvent.awayTeam);
        this.finished = false;
    }

    private void handleFinish(MatchFinishedEvent event) {
        this.finished = true;
    }

    private void handleStart(MatchStartedEvent event) {
        this.matchDate = event.matchDate;
    }

    public Match(MatchId id) {
        this.id = id;
    }

    public Match(MatchId id, String home, String away) {
        this(id);
        publishEvent(new MatchCreatedEvent(id, home, away));
    }

    public void startMatch(Date matchDate) throws MatchAlreadyStartedException {
        if (this.matchDate != null) {
            throw new MatchAlreadyStartedException("Cannot start an already started match");
        }
        publishEvent(new MatchStartedEvent(this.id, matchDate));
    }

    public void finishWithScore(Score score, Date endMatchDate) throws MatchNotStartedException {
        if (this.matchDate == null)
            System.out.print("the match has not started");
        if (!finished) {
            System.out.print("Finish the match");
            if (endMatchDate.before(this.matchDate)) {
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
