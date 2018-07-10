package fr.irit.smac.amak.messaging;

import java.util.List;

import fr.irit.smac.amak.aid.AddressableAID;

public interface IAmakMessageBox {

	IAmakAddress getAddress();

	/**
	 * Send a message to a receiver agent.
	 * 
	 * @param messageToSend
	 * @param receiver
	 * 
	 * @throws MessagingTechnicalException
	 *             if the message cannot be send this exception is throw.
	 */
	void sendMessage(IAmakMessage messageToSend, AddressableAID receiver)
			throws MessagingTechnicalException;


	void sendMessage(IAmakMessage messageToSend, AddressableAID addressableAID, IAmakMessageMetaData metadata)
			throws MessagingTechnicalException;
	
	List<IAmakEnvelope> getReceivedMessages();
}
