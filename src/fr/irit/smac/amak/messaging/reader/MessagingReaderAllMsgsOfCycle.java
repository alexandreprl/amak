package fr.irit.smac.amak.messaging.reader;

import java.util.ArrayList;
import java.util.Collection;

import fr.irit.smac.amak.messaging.IAmakEnvelope;

public class MessagingReaderAllMsgsOfCycle extends AbstractMessagingReader {



	/**
	 * Get the received messages of the current cycle.
	 * 
	 * @return the received message. The return collection can be modify.
	 **/
	public Collection<IAmakEnvelope> getMessages() {
		return new ArrayList<>(messagesOfTheCycle);
	}

}
