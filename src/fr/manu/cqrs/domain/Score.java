package fr.manu.cqrs.domain;

public class Score {
    public final int homeGoals;
    public final int awayGoals;

    public Score(int homeGoals, int awayGoals) {
        super();
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
    }

}
