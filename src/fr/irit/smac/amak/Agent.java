package fr.irit.smac.amak;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
public abstract class Agent<A extends Amas<E>, E extends Environment> {
	/**
	 * Neighborhood of the agent (must refer to the same couple amas,
	 * environment
	 */
	protected final List<Agent<A, E>> neighborhood;
	/**
	 * Criticalities of the neighbors (and it self) as perceived at the
	 * beginning of the agent's cycle
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
	 * Execution order of the agent
	 */
	private int executionOrder;

	/**
	 * The constructor automatically add the agent to the corresponding amas and
	 * initialize the agent
	 * 
	 * @param amas
	 *            Amas the agent belongs to
	 */
	public Agent(A amas) {
		this.amas = amas;
		this.amas._addAgent(this);
		neighborhood = new ArrayList<>();
		neighborhood.add(this);
		onInitialize();
	}

	/**
	 * Add neighbors to the agent
	 * 
	 * @param agents
	 *            The list of agent that should be considered as neighbor
	 */
	public final void addNeighbor(Agent<A, E>... agents) {
		for (Agent<A, E> agent : agents) {
			neighborhood.add(agent);
			criticalities.put(agent, Double.NEGATIVE_INFINITY);
		}
	}

	/**
	 * This method must be overridden by the agents. This method shouldn't make
	 * any calls to internal representation an agent has on its environment
	 * because these information maybe outdated.
	 * 
	 * @return the criticality at a given moment
	 */
	//TODO change the moment the criticality is being calculated
	protected double computeCriticality() {
		return Double.NEGATIVE_INFINITY;
	}

	/**
	 * This method must be overriden if you need to specify an execution order
	 * between agents
	 * 
	 * @return the execution order
	 */
	protected int computeExecutionOrder() {
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
	 * This method is essentially intended for debugging purpose. For example,
	 * it is a good place to display the criticality of the agent.
	 */
	protected void onDraw() {

	}

	/**
	 * Called when all initial agents have been created and are ready to be
	 * started
	 */
	protected void onReady() {

	}

	/**
	 * Called by the framework when all initial agents have been created and are
	 * almost ready to be started
	 */
	protected final void _onBeforeReady() {
		criticality = computeCriticality();
		executionOrder = computeExecutionOrder();
	}

	/**
	 * Called before all agents are created
	 */
	protected void onInitialize() {

	}

	/**
	 * Called before the cycle of the system starts
	 */
	protected void onSystemCycleBegin() {

	}

	/**
	 * Called each time the cycle of the system is over
	 */
	protected void onSystemCycleEnd() {

	}

	/**
	 * This method is called automatically and corresponds to a full cycle of an
	 * agent
	 */
	public final void cycle() {
		onAgentCycleBegin();
		for (Agent<A, E> agent : neighborhood) {
			criticalities.put(agent, agent.criticality);
		}
		onPerceiveDecideAct();
		criticality = computeCriticality();
		executionOrder = computeExecutionOrder();
		onExpose();
		onDraw();
		onAgentCycleEnd();
	}

	/**
	 * Perceive, decide and act
	 */
	protected void onPerceiveDecideAct() {
		onPerceive();
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
		return criticalest.get((int) (Math.random() * criticalest.size()));
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
}
