package fr.irit.smac.amak.messaging;

import fr.irit.smac.amak.aid.AddressableAID;

public interface IAmakMessagingService {

	IAmakMessageBox buildNewAmakMessageBox(AddressableAID aid);

	IAmakAddress buildNewAmakAddress(String randomUUID);

	void dispose();
}
