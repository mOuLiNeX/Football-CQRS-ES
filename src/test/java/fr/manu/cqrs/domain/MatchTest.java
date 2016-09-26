package fr.manu.cqrs.domain;

import static java.time.LocalDateTime.now;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import fr.manu.cqrs.domain.exception.MatchAlreadyFinishedException;
import fr.manu.cqrs.domain.exception.MatchAlreadyStartedException;
import fr.manu.cqrs.domain.exception.MatchNotStartedException;

public class MatchTest {

	@Test
	public void testStartMatch() {
		LocalDateTime startTime = now();

		// Given
		Match match = new Match(MatchId.newMatchId(), "team1", "team2");

		// When
		match.startMatch(startTime);

		// Then
		Assertions.assertThat(match.getMatchDate()).isEqualTo(startTime);
		Assertions.assertThat(match.isFinished()).isFalse();
	}

	@Test(expected = MatchAlreadyStartedException.class)
	public void testCannotStartMatchTwice() {
		LocalDateTime startTime = now();

		// Given
		Match match = new Match(MatchId.newMatchId(), "team1", "team2");

		// When
		match.startMatch(startTime);
		match.startMatch(startTime);


		// Then expect error
	}

	@Test
	public void testCanFinishAMatchWithScore() {
		LocalDateTime startTime = now();

		// Given
		Match match = new Match(MatchId.newMatchId(), "team1", "team2");
		match.startMatch(startTime);

		// When
		match.finishWithScore(new Score(1, 0), startTime.plusMinutes(90));

		// Then
		Assertions.assertThat(match.isFinished()).isTrue();
	}

	@Test(expected = MatchNotStartedException.class)
	public void testCannotFinishAMatchBeforeItStarts() {
		LocalDateTime startTime = now();

		// Given
		Match match = new Match(MatchId.newMatchId(), "team1", "team2");
		match.startMatch(startTime);

		// When
		match.finishWithScore(new Score(1, 0), startTime.minusMinutes(90));

		// Then expect error
	}

	@Test(expected = MatchNotStartedException.class)
	public void testCannotFinishANonStartedMatch() {
		LocalDateTime startTime = now();

		// Given
		Match match = new Match(MatchId.newMatchId(), "team1", "team2");

		// When
		match.finishWithScore(new Score(1, 0), startTime.minusMinutes(90));

		// Then expect error
	}

	@Test(expected = MatchAlreadyFinishedException.class)
	public void testCannotFinishMatchTwice() {
		LocalDateTime startTime = now();

		// Given
		Match match = new Match(MatchId.newMatchId(), "team1", "team2");
		match.startMatch(startTime);

		// When
		match.finishWithScore(new Score(1, 0), startTime.plusMinutes(90));
		match.finishWithScore(new Score(1, 0), startTime.plusMinutes(90));

		// Then expect error
	}
}
