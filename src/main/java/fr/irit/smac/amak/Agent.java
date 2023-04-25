package fr.irit.smac.amak;

import lombok.Getter;

import java.util.*;
import java.util.Map.Entry;

/**
 * This class must be overridden by all agents
 *
 * @param <A> The kind of Amas the agent refers to
 * @param <E> The kind of Environment the agent AND the Amas refer to
 */
public abstract class Agent<A extends Amas<E>, E extends Environment> {
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
	@Getter
	protected final A amas;
	/**
	 * The parameters that can be user in the initialization process
	 * {@link Agent#onInitialization()}
	 */
	protected final Object[] params;
	/**
	 * The id of the agent
	 */
	@Getter
	private final int id;
	/**
	 * Last calculated criticality of the agent
	 */
	private double criticality;

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
		this.amas = Objects.requireNonNull(amas);
		neighborhood = new ArrayList<>();
		neighborhood.add(this);
		onInitialization();
		this.amas.addAgent(this);
	}

	/**
	 * Add neighbors to the agent
	 *
	 * @param agents The list of agent that should be considered as neighbor
	 */
	@SafeVarargs
	public final void addNeighbor(Agent<A, E>... agents) {
		for (var agent : agents) {
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
	 * This method is called at the beginning of an agent's cycle
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onAgentCycleBegin() {
		// To be implemented
	}

	/**
	 * This method is called at the end of an agent's cycle
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onAgentCycleEnd() {
		// To be implemented
	}

	/**
	 * This method corresponds to the perception phase of the agents and must be
	 * overridden
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onPerceive() {
		// To be implemented
	}

	/**
	 * This method corresponds to the decision phase of the agents and must be
	 * overridden
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onDecide() {
		// To be implemented
	}

	/**
	 * This method corresponds to the action phase of the agents and must be
	 * overridden
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onAct() {
		// To be implemented
	}

	/**
	 * In this method the agent should expose some variables with its neighbor
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onExpose() {
		// To be implemented
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
		// To be implemented
	}

	/**
	 * Called by the framework when all initial agents have been created and are
	 * almost ready to be started
	 */
	protected final void onBeforeReady() {
		criticality = computeCriticality();
	}

	/**
	 * Called before all agents are created
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onInitialization() {
		// To be implemented
	}

	/**
	 * This method represents the perception phase of the agent
	 */
	void phase1() {
		onAgentCycleBegin();
		perceive();
	}

	/**
	 * This method represents the decisionAndAction phase of the agent
	 */
	void phase2() {
		decideAndAct();
		onExpose();
		onAgentCycleEnd();
	}

	public void cycle() {
		phase1();
		phase2();
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
		return mostCritical.get(amas.getEnvironment().getRandom().nextInt(mostCritical.size()));
	}

	/**
	 * Remove the agent from the system
	 */
	public void destroy() {
		amas.removeAgent(this);
	}

	/**
	 * Agent toString
	 */
	@Override
	public String toString() {
		return String.format("Agent/%d/", id);
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
