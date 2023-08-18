package fr.irit.smac.amak.messaging;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Mailbox {
	private int messagesCountInQueue = 0;
	private Map<Class<? extends Message>, MailStack> mailStacks = new ConcurrentHashMap<>();
	public void receive(Message message) {
		var mailStack = mailStacks.computeIfAbsent(message.getClass(), messageClass -> new MailStack());
		mailStack.receive(message);
		messagesCountInQueue++;
	}
	public<T extends Message> Optional<T> read(Class<T> tClass) {
		if (messagesCountInQueue == 0)
			return Optional.empty();
		var mailStack = mailStacks.get(tClass);
		if (mailStack != null) {
			Optional retrieved = mailStack.retrieve();
			if (retrieved.isPresent())
				messagesCountInQueue--;
			return retrieved;
		}
		return Optional.empty();
	}

	public boolean hasMessageOfType(Class<? extends Message> messageClass) {
		if (messagesCountInQueue == 0)
			return false;
		var mailStack = mailStacks.get(messageClass);
		if (mailStack != null)
			return !mailStack.isEmpty();
		return false;
	}

	public boolean hasAMessage() {
		return messagesCountInQueue!=0;
	}
}
