package fr.manu.cqrs.domain.event;

// TODO Abstraction
public final class MatchEventBus {

    private static final MatchEventBus eventBus = new MatchEventBus();

    private MatchEventBus() {
    }

    public static void register(MatchEvent handler) {
        eventBus.register(handler);
    }

    public static void post(MatchEvent event) {
        eventBus.post(event);
    }

}
