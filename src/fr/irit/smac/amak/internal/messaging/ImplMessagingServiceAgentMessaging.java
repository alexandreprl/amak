package fr.irit.smac.amak.internal.messaging;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import fr.irit.smac.amak.aid.AddressableAID;
import fr.irit.smac.amak.messaging.IAmakAddress;
import fr.irit.smac.amak.messaging.IAmakEnvelope;
import fr.irit.smac.amak.messaging.IAmakMessageBox;
import fr.irit.smac.amak.messaging.IAmakMessagingService;
import fr.irit.smac.libs.tooling.messaging.AgentMessaging;
import fr.irit.smac.libs.tooling.messaging.IMsgBox;

/**
 * An local fashion Messaging Service implementation using the agent-messaging
 * library.
 */
public class ImplMessagingServiceAgentMessaging implements IAmakMessagingService {

	private Map<String, IMsgBox<IAmakEnvelope>> mapIDMsgbox = new ConcurrentHashMap<String, IMsgBox<IAmakEnvelope>>();

	@Override
	public synchronized IAmakMessageBox buildNewAmakMessageBox(AddressableAID aid) {
		IMsgBox<IAmakEnvelope> msgbox = getOrCreateMsgbox(aid.getID());
		return new ImplMessageBoxAgentMessaging(msgbox, aid);
	}

	@Override
	public synchronized IAmakAddress getOrCreateAmakAddress(String rawID) {
		IMsgBox<IAmakEnvelope> msgbox = getOrCreateMsgbox(rawID);
		ImplAddressAgentMessaging addressOfMsgBox = new ImplAddressAgentMessaging(msgbox.getRef());
		return addressOfMsgBox;
	}

	private synchronized IMsgBox<IAmakEnvelope> getOrCreateMsgbox(String rawID) {
		IMsgBox<IAmakEnvelope> msgbox = mapIDMsgbox.get(rawID);
		if (null == msgbox) {
			msgbox = AgentMessaging.getMsgBox(rawID, IAmakEnvelope.class);
			mapIDMsgbox.put(rawID, msgbox);
		}
		return msgbox;
	}

	@Override
	public void dispose() {
		mapIDMsgbox.values().forEach(msgbox -> msgbox.dispose());
	}

}
