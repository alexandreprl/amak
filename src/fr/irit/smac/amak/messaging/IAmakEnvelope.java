package fr.irit.smac.amak.messaging;

import fr.irit.smac.amak.aid.AddressableAID;

public interface IAmakEnvelope {

	IAmakMessage getMessage();

	AddressableAID getMessageSenderAID();

}
