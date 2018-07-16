package fr.irit.smac.amak;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import fr.irit.smac.amak.aid.AID;
import fr.irit.smac.amak.aid.AddressableAID;
import fr.irit.smac.amak.internal.messaging.ImplMessagingServiceAgentMessaging;
import fr.irit.smac.amak.messaging.IAmakAddress;
import fr.irit.smac.amak.messaging.IAmakEnvelope;
import fr.irit.smac.amak.messaging.IAmakMessage;
import fr.irit.smac.amak.messaging.IAmakMessageBox;
import fr.irit.smac.amak.messaging.IAmakMessageMetaData;
import fr.irit.smac.amak.messaging.IAmakMessagingService;
import fr.irit.smac.amak.messaging.MessagingTechnicalException;

/**
 * This class must be overridden by communicant agents to use messages
 * 
 * @author Florent Mouysset
 *
 * @param <A>
 *            The kind of Amas the agent refers to
 * @param <E>
 *            The kind of Environment the agent AND the Amas refer to
 */
public abstract class AgentCommunicant<A extends Amas<E>, E extends Environment> extends Agent<A, E>
		implements Runnable {

	/**
	 * The agent identifier
	 */
	private final AddressableAID aid;

	/** The msgbox is the delegate of all the messaging services (sending/receiving msg) */
	private IAmakMessageBox messageBox;

	/** The perceived messages */
	private Collection<IAmakEnvelope> messagesOfTheCycle;

	/** Messaging services builder */
	private static final IAmakMessagingService messagingService = new ImplMessagingServiceAgentMessaging();

	/**
	 * The constructor automatically add the agent to the corresponding amas and
	 * initialize the agent
	 * 
	 * @param amas
	 *            Amas the agent belongs to
	 * @param params
	 *            The params to initialize the agent
	 */
	public AgentCommunicant(A amas, Object... params) {
		super(amas, params);
		//build the address...
		final String randomUUID = UUID.randomUUID().toString();
		IAmakAddress address = messagingService.buildNewAmakAddress(randomUUID);
		//... to build the AID and the msgbox
		this.aid = new AddressableAID(address, randomUUID);
		messageBox = messagingService.buildNewAmakMessageBox(aid);
	}


	/**
	 * Override to handle the messages received
	 */
	@Override
	final void perceive() {
		super.perceive();
		
		//event the msgbox return a list, the full order cannot be guaranty.
		// All the previous message will be erase.
		messagesOfTheCycle = messageBox.getReceivedMessages();
	}


	/**
	 * Agent toString using the AID agent.
	 */
	@Override
	public String toString() {
		return String.format("Agent #%1$-4.4s", aid);
	}


	/**
	 * @return the agent identifier object of a agent.<br>
	 *         The {@link AID} object is mandatory to send message.
	 */
	public AID getAID() {
		return aid;
	}
	
	/**
	 * Send a message to an agent.
	 * @param messageToSend
	 * @param receiver
	 * @return true if the message is correctly send, false is the message isn't send.
	 * */
	public boolean sendMessage(IAmakMessage messageToSend, AID receiver) {
		boolean result = false;
		try {
			getAddressableAIDFromAID(receiver);
			messageBox.sendMessage(messageToSend, (AddressableAID) receiver);
			result = true;
		} catch (MessagingTechnicalException exception) {
			// TODO must be log !
			exception.printStackTrace();
		}
		return result;
	}

	public boolean sendMessage(IAmakMessage messageToSend, AID receiver, IAmakMessageMetaData metadata) {
		boolean result = false;
		try {
			getAddressableAIDFromAID(receiver);
			messageBox.sendMessage(messageToSend, (AddressableAID) receiver, metadata);
			result = true;
		} catch (MessagingTechnicalException exception) {
			// TODO must be log !
			exception.printStackTrace();
		}
		return result;
	}

	private AddressableAID getAddressableAIDFromAID(AID receiver) throws MessagingTechnicalException {
		AddressableAID result = null;
		if (receiver instanceof AddressableAID) {
			result = (AddressableAID) receiver;
		} else {
			throw new MessagingTechnicalException(
					"AID type isn't adressable ! Expected type: AddressableAID but actual type is "
							+ receiver.getClass().getName());
		}
		return result;
	}

	/**
	 * Get the received messages of the current cycle.
	 * 
	 * @return the received message. The return collection can be modify.
	 **/
	public Collection<IAmakEnvelope> getAllReceivedMessages() {
		return new ArrayList<>(messagesOfTheCycle);
	}

	public <M extends IAmakMessage> Collection<M> getReceivedMessagesGivenType(Class<M> clasz) {
		@SuppressWarnings("unchecked") // it is a safe cast
		List<M> result = (List<M>) messagesOfTheCycle.stream().filter(env -> env.getMessage().getClass().equals(clasz))
				.map(env -> (M) env.getMessage()).collect(Collectors.toList());

		return result;
	}
}
