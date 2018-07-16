package fr.irit.smac.amak.messaging.reader;

import java.util.Collection;

import fr.irit.smac.amak.messaging.IAmakEnvelope;
import fr.irit.smac.amak.messaging.IAmakReadableMessageBox;

public abstract class AbstractMessagingReader implements IMessagingReader {

	/**
	 * The msgbox is the delegate of all the "retrieved operations"
	 */
	protected IAmakReadableMessageBox messageBox;

	/** The retrieved messages of cycle */
	protected Collection<IAmakEnvelope> messagesOfTheCycle;

	@Override
	public void setReadableMessageBox(IAmakReadableMessageBox messageBox) {
		this.messageBox = messageBox;
	}

	@Override
	public void readMsgbox() {
		// event the msgbox return a list, the full order cannot be guaranty.
		// All the previous message will be erase.
		messagesOfTheCycle = messageBox.getReceivedMessages();
	}
}
