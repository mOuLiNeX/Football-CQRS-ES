package fr.manu.cqrs.domain.event;

import com.google.common.eventbus.Subscribe;

// TODO A bouger (code infra)
public class MatchEventPersisterSubscriber {

    @Subscribe
    public void receivePublishedEvent(MatchEvent evt) {
        MatchEventStore.append(evt);
    }

}
