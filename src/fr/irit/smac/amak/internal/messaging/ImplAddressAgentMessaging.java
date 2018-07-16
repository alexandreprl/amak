package fr.irit.smac.amak.internal.messaging;

import fr.irit.smac.amak.messaging.IAmakAddress;
import fr.irit.smac.amak.messaging.IAmakEnvelope;
import fr.irit.smac.libs.tooling.messaging.impl.Ref;

/**
 * An Address implementation using the agent-messaging library.
 */
public class ImplAddressAgentMessaging implements IAmakAddress {
	private final Ref<IAmakEnvelope> address;

	ImplAddressAgentMessaging(Ref<IAmakEnvelope> address) {
		this.address = address;
	}

	Ref<IAmakEnvelope> getAddress() {
		return address;
	}

	@Override
	public String toString() {
		return address.getId();
	}
	
	
}
