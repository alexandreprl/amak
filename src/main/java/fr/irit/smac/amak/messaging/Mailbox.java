package fr.irit.smac.amak.messaging;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Mailbox<T extends Message> {
	private final ConcurrentLinkedQueue<T> messages = new ConcurrentLinkedQueue<>();

	public void send(T message) {
		messages.add(message);
	}

	public boolean isEmpty() {
		return messages.isEmpty();
	}
	public int size() {
		return messages.size();
	}

	public Optional<T> retrieve() {
		var message = messages.poll();
		if (message == null)
			return Optional.empty();
		return Optional.of(message);
	}
}
