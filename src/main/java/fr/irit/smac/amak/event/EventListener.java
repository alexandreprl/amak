package fr.irit.smac.amak.event;

import java.util.Collection;

public interface EventListener {
	void process(Event event);

	default void process(Collection<Event> events) {
		for (var e : events) {
			process(e);
		}
	}
}
