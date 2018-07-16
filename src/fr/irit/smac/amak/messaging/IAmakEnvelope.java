package fr.irit.smac.amak.messaging;

import fr.irit.smac.amak.aid.AddressableAID;

public interface IAmakEnvelope extends IAmakMessageMetaData {

	IAmakMessage getMessage();

	IAmakMessageMetaData getMetadata();

	AddressableAID getMessageSenderAID();

}
