package fr.irit.smac.amak.messaging;

import fr.irit.smac.amak.aid.AddressableAID;

public interface IAmakMessagingService {

	IAmakMessageBox buildNewAmakMessageBox(AddressableAID aid);

	IAmakAddress getOrCreateAmakAddress(String randomUUID);

	void disposeAll();

	void dispose(AddressableAID aid);
}
