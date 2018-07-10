package fr.irit.smac.amak;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import fr.irit.smac.amak.Amas.ExecutionPolicy;
import fr.irit.smac.amak.aid.AID;
import fr.irit.smac.amak.aid.AddressableAID;
import fr.irit.smac.amak.internal.messaging.ImplMessagingServiceAgentMessaging;
import fr.irit.smac.amak.messaging.IAmakAddress;
import fr.irit.smac.amak.messaging.IAmakEnvelope;
import fr.irit.smac.amak.messaging.IAmakMessage;
import fr.irit.smac.amak.messaging.IAmakMessageBox;
import fr.irit.smac.amak.messaging.IAmakMessagingService;
import fr.irit.smac.amak.messaging.MessagingTechnicalException;
import fr.irit.smac.amak.tools.Log;

/**
 * This class must be overridden by all agents
 * 
 * @author Alexandre Perles
 *
 * @param <A>
 *            The kind of Amas the agent refers to
 * @param <E>
 *            The kind of Environment the agent AND the Amas refer to
 */
public abstract class Agent<A extends Amas<E>, E extends Environment> implements Runnable {
	/**
	 * Neighborhood of the agent (must refer to the same couple amas, environment
	 */
	protected final List<Agent<A, E>> neighborhood;
	/**
	 * Criticalities of the neighbors (and it self) as perceived at the beginning of
	 * the agent's cycle
	 */
	protected final Map<Agent<A, E>, Double> criticalities = new HashMap<>();
	/**
	 * Last calculated criticality of the agent
	 */
	private double criticality;
	/**
	 * Amas the agent belongs to
	 */
	protected final A amas;
	/**
	 * Unique index to give unique id to each agent
	 */
	@Deprecated
	private static int uniqueIndex;

	/**
	 * The id of the agent
	 */
	// for now, leave the legacy id
	@Deprecated
	private final int id;

	/**
	 * The agent identifier
	 */
	private final AddressableAID aid;

	/**
	 * The order of execution of the agent as computed by
	 * {@link Agent#_computeExecutionOrder()}
	 */
	private int executionOrder;
	/**
	 * The parameters that can be user in the initialization process
	 * {@link Agent#onInitialization()}
	 */
	protected Object[] params;

	/**
	 * These phases are used to synchronize agents on phase
	 * 
	 * @see fr.irit.smac.amak.Amas.ExecutionPolicy
	 * @author perles
	 *
	 */
	public enum Phase {
		/**
		 * Agent is perceiving
		 */
		PERCEPTION,
		/**
		 * Agent is deciding and acting
		 */
		DECISION_AND_ACTION,
		/**
		 * Agent haven't started to perceive, decide or act
		 */
		INITIALIZING,
		/**
		 * Agent is ready to decide
		 */
		PERCEPTION_DONE,
		/**
		 * Agent is ready to perceive or die
		 */
		DECISION_AND_ACTION_DONE
	}

	/**
	 * The current phase of the agent {@link Phase}
	 */
	protected Phase currentPhase = Phase.INITIALIZING;

	/** The msgbox is the delegate of all the messaging services (sending/receiving msg) */
	private IAmakMessageBox messageBox;

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
	public Agent(A amas, Object... params) {
		this.id = uniqueIndex++;

		//build the address...
		final String randomUUID = UUID.randomUUID().toString();
		IAmakAddress address = messagingService.buildNewAmakAddress(randomUUID);
		//... to build the AID and the msgbox
		this.aid = new AddressableAID(address, randomUUID);
		messageBox = messagingService.buildNewAmakMessageBox(aid);
		this.params = params;
		this.amas = amas;
		if (amas != null) {
			this.amas._addAgent(this);
		}
		neighborhood = new ArrayList<>();
		neighborhood.add(this);
		onInitialization();
		if (!Configuration.commandLineMode)
			onRenderingInitialization();
	}

	/**
	 * Add neighbors to the agent
	 * 
	 * @param agents
	 *            The list of agent that should be considered as neighbor
	 */
	@SafeVarargs
	public final void addNeighbor(Agent<A, E>... agents) {
		for (Agent<A, E> agent : agents) {
			if (agent != null) {
				neighborhood.add(agent);
				criticalities.put(agent, Double.NEGATIVE_INFINITY);
			}
		}
	}

	/**
	 * This method must be overridden by the agents. This method shouldn't make any
	 * calls to internal representation an agent has on its environment because
	 * these information maybe outdated.
	 * 
	 * @return the criticality at a given moment
	 */
	protected double computeCriticality() {
		return Double.NEGATIVE_INFINITY;
	}

	/**
	 * This method must be overriden if you need to specify an execution order layer
	 * 
	 * @return the execution order layer
	 */
	protected int computeExecutionOrderLayer() {
		return 0;
	}

	/**
	 * This method is called at the beginning of an agent's cycle
	 */
	protected void onAgentCycleBegin() {

	}

	/**
	 * This method is called at the end of an agent's cycle
	 */
	protected void onAgentCycleEnd() {

	}

	/**
	 * This method corresponds to the perception phase of the agents and must be
	 * overridden
	 */
	protected void onPerceive() {

	}

	/**
	 * This method corresponds to the decision phase of the agents and must be
	 * overridden
	 */
	protected void onDecide() {

	}

	/**
	 * This method corresponds to the action phase of the agents and must be
	 * overridden
	 */
	protected void onAct() {

	}

	/**
	 * In this method the agent should expose some variables with its neighbor
	 */
	protected void onExpose() {

	}

	/**
	 * This method should be used to update the representation of the agent for
	 * example in a VUI
	 */
	protected void onUpdateRender() {

	}

	/**
	 * This method is now deprecated and should be replaced by onUpdateRender
	 * 
	 * @deprecated Must be replaced by {@link #onUpdateRender()}
	 */
	@Deprecated
	protected final void onDraw() {

	}

	/**
	 * Called when all initial agents have been created and are ready to be started
	 */
	protected void onReady() {

	}

	/**
	 * Called by the framework when all initial agents have been created and are
	 * almost ready to be started
	 */
	protected final void _onBeforeReady() {
		criticality = computeCriticality();
		executionOrder = _computeExecutionOrder();
	}

	/**
	 * Called before all agents are created
	 */
	protected void onInitialization() {

	}

	/**
	 * Replaced by onInitialization
	 * 
	 * @deprecated Must be replaced by {@link #onInitialization()}
	 */
	@Deprecated
	protected final void onInitialize() {

	}

	/**
	 * Called to initialize the rendering of the agent
	 */
	protected void onRenderingInitialization() {

	}

	/**
	 * @deprecated This method is useless because the state of the agent is not
	 *             supposed to evolve before or after its cycle. Use
	 *             OnAgentCycleBegin/End instead.
	 * 
	 *             This method is final because it must not be implemented.
	 *             Implement it will have no effect.
	 */
	@Deprecated
	protected final void onSystemCycleBegin() {

	}

	/**
	 * @deprecated This method is useless because the state of the agent is not
	 *             supposed to evolve before or after its cycle. Use
	 *             OnAgentCycleBegin/End instead.
	 * 
	 *             This method is final because it must not be implemented.
	 *             Implement it will have no effect.
	 */
	@Deprecated
	protected final void onSystemCycleEnd() {

	}

	/**
	 * This method is called automatically and corresponds to a full cycle of an
	 * agent
	 */
	@Override
	public void run() {
		ExecutionPolicy executionPolicy = amas.getExecutionPolicy();
		if (executionPolicy == ExecutionPolicy.TWO_PHASES) {

			currentPhase = nextPhase();
			switch (currentPhase) {
			case PERCEPTION:
				phase1();
				amas.informThatAgentPerceptionIsFinished();
				break;
			case DECISION_AND_ACTION:
				phase2();
				amas.informThatAgentDecisionAndActionAreFinished();
				break;
			default:
				Log.fatal("AMAK", "An agent is being run in an invalid phase (%s)", currentPhase);
			}
		} else if (executionPolicy == ExecutionPolicy.ONE_PHASE) {
			currentPhase = Phase.PERCEPTION;
			phase1();
			currentPhase = Phase.DECISION_AND_ACTION;
			phase2();
			amas.informThatAgentPerceptionIsFinished();
			amas.informThatAgentDecisionAndActionAreFinished();
		}
	}

	/**
	 * This method represents the perception phase of the agent
	 */
	protected final void phase1() {
		onAgentCycleBegin();
		perceive();
		currentPhase = Phase.PERCEPTION_DONE;
	}

	/**
	 * This method represents the decisionAndAction phase of the agent
	 */
	protected final void phase2() {
		decideAndAct();
		executionOrder = _computeExecutionOrder();
		onExpose();
		if (!Configuration.commandLineMode)
			onUpdateRender();
		onAgentCycleEnd();
		currentPhase = Phase.DECISION_AND_ACTION_DONE;
	}

	/**
	 * Determine which phase comes after another
	 * 
	 * @return the next phase
	 */
	private Phase nextPhase() {
		switch (currentPhase) {
		case PERCEPTION_DONE:
			return Phase.DECISION_AND_ACTION;
		case INITIALIZING:
		case DECISION_AND_ACTION_DONE:
		default:
			return Phase.PERCEPTION;
		}
	}

	/**
	 * Compute the execution order from the layer and a random value. This method
	 * shouldn't be overridden.
	 * 
	 * @return A number used by amak to determine which agent executes first
	 */
	protected int _computeExecutionOrder() {
		return computeExecutionOrderLayer() * 10000 + amas.getEnvironment().getRandom().nextInt(10000);
	}

	/**
	 * Perceive, decide and act
	 */
	private final void perceive() {
		for (Agent<A, E> agent : neighborhood) {
			criticalities.put(agent, agent.criticality);
		}
		onPerceive();
		// Criticality of agent should be updated after perception AND after action
		criticality = computeCriticality();
		criticalities.put(this, criticality);
	}

	/**
	 * A combination of decision and action as called by the framework
	 */
	private final void decideAndAct() {
		onDecideAndAct();

		criticality = computeCriticality();
	}

	/**
	 * Decide and act These two phases can often be grouped
	 */
	protected void onDecideAndAct() {
		onDecide();
		onAct();
	}

	/**
	 * Convenient method giving the most critical neighbor at a given moment
	 * 
	 * @param includingMe
	 *            Should the agent also consider its own criticality
	 * @return the most critical agent
	 */
	protected final Agent<A, E> getMostCriticalNeighbor(boolean includingMe) {
		List<Agent<A, E>> criticalest = new ArrayList<>();
		double maxCriticality = Double.NEGATIVE_INFINITY;

		if (includingMe) {
			criticalest.add(this);
			maxCriticality = criticalities.getOrDefault(criticalest, Double.NEGATIVE_INFINITY);
		}
		for (Entry<Agent<A, E>, Double> e : criticalities.entrySet()) {
			if (e.getValue() > maxCriticality) {
				criticalest.clear();
				maxCriticality = e.getValue();
				criticalest.add(e.getKey());
			} else if (e.getValue() == maxCriticality) {
				criticalest.add(e.getKey());
			}
		}
		if (criticalest.isEmpty())
			return null;
		return criticalest.get(getEnvironment().getRandom().nextInt(criticalest.size()));
	}

	/**
	 * Get the latest computed execution order
	 * 
	 * @return the execution order
	 */
	public int getExecutionOrder() {
		return executionOrder;
	}

	/**
	 * Getter for the AMAS
	 * 
	 * @return the amas
	 */
	public A getAmas() {
		return amas;
	}

	/**
	 * Remove the agent from the system
	 */
	public void destroy() {
		getAmas()._removeAgent(this);
	}

	/**
	 * Agent toString
	 */
	@Override
	public String toString() {
		return String.format("Agent #%1$-4.4s", aid);
	}

	/**
	 * Getter for the current phase of the agent
	 * 
	 * @return the current phase
	 */
	public Phase getCurrentPhase() {
		return currentPhase;
	}

	/**
	 * Return the id of the agent
	 * 
	 * @return the id of the agent
	 * @deprecated : use {@link Agent#getAID()} to get the {@link AID} object
	 *             instead of pain/raw int id.
	 */
	@Deprecated
	public int getId() {
		return id;
	}

	/**
	 * @return the agent identifier object of a agent.<br>
	 *         The {@link AID} object is mandatory to send message.
	 */
	public AID getAID() {
		return aid;
	}
	
	/**
	 * Getter for the environment
	 * 
	 * @return the environment
	 */
	public E getEnvironment() {
		return getAmas().getEnvironment();
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
	 *Get the received messages of the current cycle.<br/>
	 *<b>After the call the msgbox is empty until the next message receiving.</b>
	 *
	 *@return the received message.
	 **/
	public Collection<IAmakEnvelope> getReceivedMessages() {
		//event the msgbox return a list, the full order cannot be guaranty.
		return messageBox.getReceivedMessages();
	}
}
