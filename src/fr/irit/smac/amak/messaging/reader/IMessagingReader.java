package fr.irit.smac.amak.messaging.reader;

import java.util.Collection;

import fr.irit.smac.amak.messaging.IAmakEnvelope;
import fr.irit.smac.amak.messaging.IAmakReadableMessageBox;

/**
 * Interface of messaging reader strategies.</br>
 * Each of strategy offer different ways to get the received messages but have
 * the same methods.
 */
public interface IMessagingReader {

	/**
	 * Retrieve the new received messages.
	 */
	void readMsgbox();

	/**
	 * Get the messages according to the strategy implementation.
	 */
	Collection<IAmakEnvelope> getMessages();

	void setReadableMessageBox(IAmakReadableMessageBox messageBox);

}
