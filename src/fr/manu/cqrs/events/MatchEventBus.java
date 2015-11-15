package fr.manu.cqrs.events;

import javax.inject.Singleton;

import com.google.common.eventbus.EventBus;

@Singleton
public final class MatchEventBus {

	private final EventBus eventBus;

	public MatchEventBus() {
		eventBus = new EventBus();
	}

	public void register(Object... subscribers) {
		for (Object subscriber : subscribers) {
			eventBus.register(subscriber);
		}
	}

	public void publishEvent(MatchEvent event) {
		eventBus.post(event);
	}

}
