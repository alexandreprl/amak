package fr.irit.smac.amak.messaging;

import fr.irit.smac.amak.aid.AddressableAID;

public interface IAmakMessageBox extends IAmakReadableMessageBox {

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

	void sendMessage(IAmakMessage messageToSend, IAmakAddress receiverAddress) throws MessagingTechnicalException;

	void sendMessage(IAmakMessage messageToSend, IAmakAddress receiverAddress, IAmakMessageMetaData metadata)
			throws MessagingTechnicalException;
	
}
