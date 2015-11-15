package fr.manu.cqrs.domain;

import java.time.LocalDateTime;

import fr.manu.cqrs.domain.exception.MatchAlreadyFinishedException;
import fr.manu.cqrs.domain.exception.MatchAlreadyStartedException;
import fr.manu.cqrs.domain.exception.MatchNotStartedException;

public class Match {
	public final MatchId id;

	private LocalDateTime matchDate;

	private Team teams[] = new Team[2];

	private boolean finished;

	public Match(MatchId id, String home, String away) {
		this.id = id;
		this.teams[0] = new Team(home);
		this.teams[1] = new Team(away);
		this.finished = false;
	}

	public void startMatch(LocalDateTime matchDate) throws MatchAlreadyStartedException {
		if (this.matchDate != null) {
			throw new MatchAlreadyStartedException("Cannot start an already started match");
		}
		this.matchDate = matchDate;
	}

	public void finishWithScore(Score score, LocalDateTime endMatchDate)
			throws MatchNotStartedException, MatchAlreadyFinishedException {
		if (finished) {
			throw new MatchAlreadyFinishedException();
		}

		if (this.matchDate == null) {
			throw new MatchNotStartedException("the match has not started");
		}
		if (endMatchDate.isBefore(this.matchDate)) {
			throw new MatchNotStartedException("Could not finish a non started match");
		}
		this.finished = true;
		System.out.print("Finish the match");
	}

	public String getHomeTeamName() {
		return teams[0].name;
	}

	public String getAwayTeamName() {
		return teams[1].name;
	}

	public LocalDateTime getMatchDate() {
		return matchDate;
	}

	public boolean isFinished() {
		return finished;
	}
}
