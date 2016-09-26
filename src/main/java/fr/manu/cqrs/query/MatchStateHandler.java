package fr.manu.cqrs.query;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;

import fr.manu.cqrs.domain.MatchId;
import fr.manu.cqrs.events.MatchCreatedEvent;
import fr.manu.cqrs.events.MatchFinishedEvent;
import fr.manu.cqrs.events.MatchStartedEvent;
import fr.manu.cqrs.query.MatchState.Status;

@Singleton
public class MatchStateHandler {

	@Inject
	MatchQuery query;

    Map<MatchId, MatchState> data;

    public MatchStateHandler() {
        data = Maps.newHashMap();
    }

    @Subscribe
    public void handle(MatchFinishedEvent event) {
		query.deleteMatch(data.get(event.matchId));
		data.remove(event.matchId);
    }

    @Subscribe
    public void handle(MatchCreatedEvent event) {
		MatchId id = event.matchId;
        MatchState state = new MatchState(id);
        data.put(id, state);
		query.addMatch(state);
    }

    @Subscribe
    public void handle(MatchStartedEvent event) {
		MatchState state = data.get(event.matchId);
        state.status = Status.STARTED;
    }
}
