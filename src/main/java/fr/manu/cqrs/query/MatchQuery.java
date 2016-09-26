package fr.manu.cqrs.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Singleton;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import fr.manu.cqrs.domain.MatchId;

@Singleton
public class MatchQuery {
	private final List<MatchState> states = Lists.newArrayList();

	public Collection<MatchState> findAll() {
		return states;
	}

	public MatchState findById(final MatchId id) {
		return Iterables.find(states, input -> input.id.equals(id));
	}

	void addMatch(MatchState... stat) {
		states.addAll(Arrays.asList(stat));
	}

	void deleteMatch(MatchState... stat) {
		states.removeAll(Arrays.asList(stat));
	}

	public void init() {
		states.clear();
	}

}
