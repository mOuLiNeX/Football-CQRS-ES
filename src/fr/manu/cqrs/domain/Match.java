package fr.manu.cqrs.domain;

import java.util.Date;

import fr.manu.cqrs.domain.event.Event;
import fr.manu.cqrs.domain.event.MatchCreatedEvent;
import fr.manu.cqrs.domain.event.MatchFinishedEvent;
import fr.manu.cqrs.domain.event.MatchStartedEvent;

public class Match extends AggregateRoot {
    public final MatchId id;

    private Date matchDate;

    private Team teams[] = new Team[2];

    private boolean finished;

    private void publishEvent(Event event) {
        // TODO Auto-generated method stub

    }

    private void handle(MatchCreatedEvent matchCreatedEvent) {
        this.teams[0] = new Team(matchCreatedEvent.matchTeamName1);
        this.teams[1] = new Team(matchCreatedEvent.matchTeamName2);
        this.finished = false;
    }

    private void handle(MatchFinishedEvent event) {
        this.finished = true;
    }

    private void handle(MatchStartedEvent event) {
        this.matchDate = event.matchDate;
    }

    public Match(MatchId id) {
        this.id = id;
    }

    public Match(MatchId id, String home, String away) {
        this(id);
        publishEvent(new MatchCreatedEvent(id, home, away));
    }

    public void startMatch(Date matchDate) {
        if (this.matchDate != null)
            System.out.print("the match has started");
        publishEvent(new MatchStartedEvent(this.id, matchDate));
    }

    public void finishWithScore(Score score, Date matchDate) {
        if (this.matchDate == null)
            System.out.print("the match has not started");
        if (finished)
            System.out.print("the match has finished");
        publishEvent(new MatchFinishedEvent(this.id, matchDate, score.homeGoals, score.awayGoals));
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
