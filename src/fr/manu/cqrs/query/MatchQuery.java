package fr.manu.cqrs.query;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
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
        return Iterables.find(states, new Predicate<MatchState>() {

            @Override
            public boolean apply(MatchState input) {
                return input.id.equals(id);
            }
        });
    }

    static void addMatch(MatchState... stat) {
        for (MatchState matchState : stat) {
            states.add(matchState);
        }
    }

    static void deleteMatch(MatchState... stat) {
        for (MatchState matchState : stat) {
            states.remove(matchState);
        }
    }

    public static void init() {
        states.clear();
    }

}
