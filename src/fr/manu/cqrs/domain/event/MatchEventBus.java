package fr.manu.cqrs.domain.event;

import com.google.common.eventbus.EventBus;

// TODO Abstraction
public final class MatchEventBus {

    private static final EventBus eventBus = new EventBus();

    private MatchEventBus() {
    }

    public static void register(Object subscriber) {
        eventBus.register(subscriber);
    }

    public static void publishEvent(MatchEvent event) {
        eventBus.post(event);
    }

}
