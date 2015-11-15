package fr.manu.cqrs.domain;

import java.util.Objects;

public class Score {
    public final int homeGoals;
    public final int awayGoals;

    public Score(int homeGoals, int awayGoals) {
        super();
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Score other = (Score) obj;
		return Objects.equals(this.homeGoals, other.homeGoals) && Objects.equals(this.awayGoals, other.awayGoals);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.homeGoals, this.awayGoals);
	}
}
