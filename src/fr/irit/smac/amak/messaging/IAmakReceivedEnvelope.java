package fr.irit.smac.amak.messaging;

import fr.irit.smac.amak.aid.AddressableAID;

public interface IAmakReceivedEnvelope<T extends IAmakMessage, M extends IAmakMessageMetaData, I extends AddressableAID> {

	T getMessage();

	M getMetadata();

	I getMessageSenderAID();

}
