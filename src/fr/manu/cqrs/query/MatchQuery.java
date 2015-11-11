package fr.manu.cqrs.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import fr.manu.cqrs.domain.MatchId;

// TODO A bouger (code infra)
public class MatchQuery {
	private static final List<MatchState> states = Lists.newArrayList();

	public static Collection<MatchState> findAll() {
		return states;
	}

	public static MatchState findById(final MatchId id) {
		return Iterables.find(states, input -> input.id.equals(id));
	}

	static void addMatch(MatchState... stat) {
		states.addAll(Arrays.asList(stat));
	}

	static void deleteMatch(MatchState... stat) {
		states.removeAll(Arrays.asList(stat));
	}

	public static void init() {
		states.clear();
	}

}
