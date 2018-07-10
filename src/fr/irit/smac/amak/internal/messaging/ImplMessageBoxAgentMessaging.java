package fr.irit.smac.amak.internal.messaging;

import java.util.List;

import fr.irit.smac.amak.aid.AddressableAID;
import fr.irit.smac.amak.messaging.IAmakAddress;
import fr.irit.smac.amak.messaging.IAmakEnvelope;
import fr.irit.smac.amak.messaging.IAmakMessage;
import fr.irit.smac.amak.messaging.IAmakMessageBox;
import fr.irit.smac.amak.messaging.MessagingTechnicalException;
import fr.irit.smac.libs.tooling.messaging.IMsgBox;
import fr.irit.smac.libs.tooling.messaging.impl.Ref;

public class ImplMessageBoxAgentMessaging implements IAmakMessageBox {
	private final IMsgBox<IAmakEnvelope> msgbox;
	private final AddressableAID senderAID;

	ImplMessageBoxAgentMessaging(IMsgBox<IAmakEnvelope> msgbox, AddressableAID aid) {
		this.msgbox = msgbox;
		this.senderAID = aid;
	}

	@Override
	public IAmakAddress getAddress() {
		return senderAID.getAgentAdress();
	}

	@Override
	public void sendMessage(IAmakMessage messageToSend, AddressableAID receiver) throws MessagingTechnicalException {
		Ref<IAmakEnvelope> refReceiver = getRefFromAmakAddress(receiver.getAgentAdress());
		IAmakEnvelope enveloppeToSend = encapsulateAmakMessage(messageToSend);
		boolean sendingSuccessful = msgbox.send(enveloppeToSend, refReceiver);
		if (!sendingSuccessful) {
			throw new MessagingTechnicalException("Unknown reason. Maybe the receiver messagebox is full, try later.");
		}
	}

	private IAmakEnvelope encapsulateAmakMessage(IAmakMessage messageToSend) {
		// other meta data can be add ...
		SimpleEnvelope envelop = new SimpleEnvelope(messageToSend, senderAID);
		return envelop;
	}

	private Ref<IAmakEnvelope> getRefFromAmakAddress(IAmakAddress iAmakAddress) throws MessagingTechnicalException {
		Ref<IAmakEnvelope> ref = null;
		if (iAmakAddress instanceof ImplAddressAgentMessaging) {
			ImplAddressAgentMessaging implem = (ImplAddressAgentMessaging) iAmakAddress;
			ref = implem.getAddress();
		} else {
			throw new MessagingTechnicalException(
					"Address type isn't compatible with the msgbox implementation. Expected type: ImplAddressAgentMessaging but actual type is "
							+ iAmakAddress.getClass().getName());
		}
		return ref;
	}

	@Override
	public List<IAmakEnvelope> getReceivedMessages() {
		return msgbox.getMsgs();
	}

}
