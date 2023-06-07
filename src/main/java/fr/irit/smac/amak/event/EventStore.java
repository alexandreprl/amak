package fr.irit.smac.amak.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventStore<T> {
	private final List<EventListener> listeners = Collections.synchronizedList(new ArrayList<>());
	private final Queue<Event> eventsHistory = new ConcurrentLinkedQueue<>();

	public void add(Event event) {
		eventsHistory.add(event);
		for (var l : listeners) {
			l.process(event);
		}
	}

	public void register(EventListener eventListener) {
		listeners.add(eventListener);
		eventListener.process(Collections.unmodifiableCollection(eventsHistory));
	}

	public void unregister(EventListener eventListener) {
		listeners.remove(eventListener);
	}
}
