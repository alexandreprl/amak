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
	public IAmakMessageBox buildNewAmakMessageBox(AddressableAID aid) {
		IMsgBox<IAmakEnvelope> msgbox = getOrCreateMsgbox(aid.getID());
		IAmakMessageBox msgboxAmak = new ImplMessageBoxAgentMessaging(msgbox, aid);
		return msgboxAmak;
	}

	@Override
	public IAmakAddress buildNewAmakAddress(String rawID) {
		IMsgBox<IAmakEnvelope> msgbox = getOrCreateMsgbox(rawID);
		ImplAddressAgentMessaging addressOfMsgBox = new ImplAddressAgentMessaging(msgbox.getRef());
		return addressOfMsgBox;
	}

	private IMsgBox<IAmakEnvelope> getOrCreateMsgbox(String rawID) {
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
