package fr.irit.smac.amak;

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
import fr.irit.smac.amak.messaging.reader.IMessagingReader;
import fr.irit.smac.amak.messaging.reader.MessagingReaderAllMsgsOfCycle;

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

	/**
	 * The msgbox is the delegate of all the messaging operations
	 * (sending/receiving msg).</br>
	 * <i>For reading the msgbox, the {@link AgentCommunicant#msgReader} should
	 * be use.</i>
	 */
	private IAmakMessageBox messageBox;

	/**
	 * The messaging reader is use to apply reading strategy on msgbox and give
	 * the msgs with a specific way.
	 */
	private IMessagingReader msgReader;

	/** Messaging services builder */
	private static final IAmakMessagingService messagingService = new ImplMessagingServiceAgentMessaging();

	/**
	 * The constructor automatically add the agent to the corresponding amas and
	 * initialize the agent.</br>
	 * The default messaging reader ({@link MessagingReaderAllMsgsOfCycle}) will
	 * be use.
	 * 
	 * @param amas
	 *            Amas the agent belongs to
	 * @param params
	 *            The params to initialize the agent
	 */
	public AgentCommunicant(A amas, Object... params) {
		this(amas, new MessagingReaderAllMsgsOfCycle(), params);
	}

	/**
	 * The constructor automatically add the agent to the corresponding amas and
	 * initialize the agent.</br>
	 * 
	 * @param amas
	 *            Amas the agent belongs to
	 * @param params
	 *            The params to initialize the agent
	 * @param msgReader
	 *            The strategy of messaging reader.
	 */
	public AgentCommunicant(A amas, IMessagingReader msgReader, Object... params) {
		super(amas, params);
		// build the address...
		final String randomUUID = UUID.randomUUID().toString();
		IAmakAddress address = messagingService.buildNewAmakAddress(randomUUID);
		// ... to build the AID and the msgbox
		this.aid = new AddressableAID(address, randomUUID);
		messageBox = messagingService.buildNewAmakMessageBox(aid);
		this.msgReader = msgReader;
		this.msgReader.setReadableMessageBox(messageBox);
	}

	/**
	 * Override to handle the messages received
	 */
	@Override
	final void perceive() {
		super.perceive();
		msgReader.readMsgbox();
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
	 * Get the received messages according to the message reader strategy. .
	 * 
	 * @return the received message.
	 **/
	public Collection<IAmakEnvelope> getAllMessages() {
		return msgReader.getMessages();
	}

	public <M extends IAmakMessage> Collection<M> getReceivedMessagesGivenType(Class<M> clasz) {
		@SuppressWarnings("unchecked") // it is a safe cast
		List<M> result = (List<M>) getAllMessages().stream().filter(env -> env.getMessage().getClass().equals(clasz))
				.map(env -> (M) env.getMessage()).collect(Collectors.toList());

		return result;
	}
}
