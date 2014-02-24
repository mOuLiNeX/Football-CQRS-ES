package fr.manu.cqrs.query;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;

import fr.manu.cqrs.domain.MatchId;
import fr.manu.cqrs.domain.event.MatchCreatedEvent;
import fr.manu.cqrs.domain.event.MatchFinishedEvent;
import fr.manu.cqrs.domain.event.MatchStartedEvent;
import fr.manu.cqrs.query.MatchState.Status;

public class MatchStateHandler {

    Map<MatchId, MatchState> data;

    public MatchStateHandler() {
        data = Maps.newHashMap();
    }

    @Subscribe
    public void handle(MatchFinishedEvent event) {
        MatchQuery.deleteMatch(data.get(event.getId()));
        data.remove(event.getId());
    }

    @Subscribe
    public void handle(MatchCreatedEvent event) {
        MatchId id = event.getId();
        MatchState state = new MatchState(id);
        data.put(id, state);
        MatchQuery.addMatch(state);
    }

    @Subscribe
    public void handle(MatchStartedEvent event) {
        MatchState state = data.get(event.getId());
        state.status = Status.STARTED;
    }
}
