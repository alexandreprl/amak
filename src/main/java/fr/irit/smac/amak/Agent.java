package fr.irit.smac.amak;

import fr.irit.smac.amak.Amas.ExecutionPolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class must be overridden by all agents
 *
 * @param <A> The kind of Amas the agent refers to
 * @param <E> The kind of Environment the agent AND the Amas refer to
 * @author Alexandre Perles
 */
public abstract class Agent<A extends Amas<E>, E extends Environment> implements Runnable {
	/**
	 * Unique index to give unique id to each agent
	 */
	private static int uniqueIndex;
	/**
	 * Neighborhood of the agent (must refer to the same couple amas, environment
	 */
	protected final List<Agent<A, E>> neighborhood;
	/**
	 * Criticalities of the neighbors (and itself) as perceived at the beginning of
	 * the agent's cycle
	 */
	protected final Map<Agent<A, E>, Double> criticalities = new HashMap<>();
	/**
	 * Amas the agent belongs to
	 */
	protected final A amas;
	/**
	 * The parameters that can be user in the initialization process
	 * {@link Agent#onInitialization()}
	 */
	protected final Object[] params;
	/**
	 * The id of the agent
	 */
	private final int id;
	/**
	 * The current phase of the agent {@link Phase}
	 */
	protected Phase currentPhase = Phase.INITIALIZING;
	/**
	 * Last calculated criticality of the agent
	 */
	private double criticality;
	/**
	 * The order of execution of the agent as computed by
	 * {@link Agent#computeExecutionOrder()}
	 */
	private int executionOrder;

	/**
	 * The constructor automatically add the agent to the corresponding amas and
	 * initialize the agent
	 *
	 * @param amas   Amas the agent belongs to
	 * @param params The params to initialize the agent
	 */
	protected Agent(A amas, Object... params) {
		this.id = uniqueIndex++;
		this.params = params;
		this.amas = amas;
		neighborhood = new ArrayList<>();
		neighborhood.add(this);
		onInitialization();


		if (amas != null) {
			this.amas.addAgent(this);
		}
	}

	/**
	 * Add neighbors to the agent
	 *
	 * @param agents The list of agent that should be considered as neighbor
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
	@SuppressWarnings("SameReturnValue")
	protected double computeCriticality() {
		return Double.NEGATIVE_INFINITY;
	}

	/**
	 * This method must be overridden if you need to specify an execution order layer
	 *
	 * @return the execution order layer
	 */
	@SuppressWarnings("SameReturnValue")
	protected int computeExecutionOrderLayer() {
		return 0;
	}

	/**
	 * This method is called at the beginning of an agent's cycle
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onAgentCycleBegin() {

	}

	/**
	 * This method is called at the end of an agent's cycle
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onAgentCycleEnd() {

	}

	/**
	 * This method corresponds to the perception phase of the agents and must be
	 * overridden
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onPerceive() {

	}

	/**
	 * This method corresponds to the decision phase of the agents and must be
	 * overridden
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onDecide() {

	}

	/**
	 * This method corresponds to the action phase of the agents and must be
	 * overridden
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onAct() {

	}

	/**
	 * In this method the agent should expose some variables with its neighbor
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onExpose() {

	}

	/**
	 * This method should be used to update the representation of the agent for
	 * <p>
	 * <p>
	 * /**
	 * Called when all initial agents have been created and are ready to be started
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onReady() {

	}

	/**
	 * Called by the framework when all initial agents have been created and are
	 * almost ready to be started
	 */
	protected final void onBeforeReady() {
		criticality = computeCriticality();
		executionOrder = computeExecutionOrder();
	}

	/**
	 * Called before all agents are created
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onInitialization() {

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
				case PERCEPTION -> {
					phase1();
					amas.informThatAgentPerceptionIsFinished();
				}
				case DECISION_AND_ACTION -> {
					phase2();
					amas.informThatAgentDecisionAndActionAreFinished();
				}
				default -> throw new IllegalStateException("Unexpected value: " + currentPhase);
			}
		} else if (executionPolicy == ExecutionPolicy.ONE_PHASE) {
			onePhaseCycle();
			amas.informThatAgentPerceptionIsFinished();
			amas.informThatAgentDecisionAndActionAreFinished();
		}
	}

	public void onePhaseCycle() {
		currentPhase = Phase.PERCEPTION;
		phase1();
		currentPhase = Phase.DECISION_AND_ACTION;
		phase2();
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
		executionOrder = computeExecutionOrder();
		onExpose();
		onAgentCycleEnd();
		currentPhase = Phase.DECISION_AND_ACTION_DONE;
	}

	/**
	 * Determine which phase comes after another
	 *
	 * @return the next phase
	 */
	private Phase nextPhase() {
		return switch (currentPhase) {
			case PERCEPTION_DONE -> Phase.DECISION_AND_ACTION;
			default -> Phase.PERCEPTION;
		};
	}

	/**
	 * Compute the execution order from the layer and a random value. This method
	 * shouldn't be overridden.
	 *
	 * @return A number used by amak to determine which agent executes first
	 */
	private int computeExecutionOrder() {
		return computeExecutionOrderLayer() * 10000 + amas.getEnvironment().getRandom().nextInt(10000);
	}

	/**
	 * Perceive, decide and act
	 */
	void perceive() {
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
	private void decideAndAct() {
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
	 * @param includingMe Should the agent also consider its own criticality
	 * @return the most critical agent
	 */
	protected final Agent<A, E> getMostCriticalNeighbor(boolean includingMe) {
		var mostCritical = new ArrayList<Agent<A, E>>();
		double maxCriticality = Double.NEGATIVE_INFINITY;

		if (includingMe) {
			mostCritical.add(this);
			maxCriticality = criticalities.getOrDefault(this, Double.NEGATIVE_INFINITY);
		}
		for (Entry<Agent<A, E>, Double> e : criticalities.entrySet()) {
			if (e.getValue() > maxCriticality) {
				mostCritical.clear();
				maxCriticality = e.getValue();
				mostCritical.add(e.getKey());
			} else if (e.getValue() == maxCriticality) {
				mostCritical.add(e.getKey());
			}
		}
		if (mostCritical.isEmpty())
			return null;
		return mostCritical.get(getEnvironment().getRandom().nextInt(mostCritical.size()));
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
		getAmas().removeAgent(this);
	}

	/**
	 * Agent toString
	 */
	@Override
	public String toString() {
		return String.format("Agent #%d", id);
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
	 */
	public int getId() {
		return id;
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
	 * These phases are used to synchronize agents on phase
	 *
	 * @author perles
	 * @see fr.irit.smac.amak.Amas.ExecutionPolicy
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
}
