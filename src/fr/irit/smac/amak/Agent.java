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
	private static int uniqueIndex;

	/**
	 * The id of the agent
	 */
	private final int id = uniqueIndex++;
	private int executionOrder;
	protected Object[] params;

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
		this.params = params;
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
	 * This method is essentially intended for debugging purpose. For example, it is
	 * a good place to display the criticality of the agent.
	 */
	protected void onDraw() {

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
		executionOrder = computeExecutionOrder();
	}

	/**
	 * Called before all agents are created
	 */
	protected void onInitialize() {

	}

	/**
	 * These methods are useless because the state of the agent is not supposed to
	 * evolve before or after its cycle. Use OnAgentCycleBegin/End instead.
	 * 
	 * These methods are finals because they must not be implemented. Implement them
	 * will have no effect.
	 */
	@Deprecated
	protected final void onSystemCycleBegin() {

	}

	@Deprecated
	protected final void onSystemCycleEnd() {

	}

	/**
	 * This method is called automatically and corresponds to a full cycle of an
	 * agent
	 */
	@Override
	public final void run() {
		onAgentCycleBegin();
		for (Agent<A, E> agent : neighborhood) {
			criticalities.put(agent, agent.criticality);
		}
		_onPerceiveDecideAct();
		executionOrder = computeExecutionOrder();
		onExpose();
		onDraw();
		onAgentCycleEnd();
		amas.informThatAgentCycleIsFinished();
	}

	/**
	 * Compute the execution order from the layer and a random value
	 * 
	 * @return
	 */
	private final int computeExecutionOrder() {
		return computeExecutionOrderLayer() * 10000 + amas.getEnvironment().getRandom().nextInt(10000);
	}

	/**
	 * Perceive, decide and act
	 */
	private final void _onPerceiveDecideAct() {
		onPerceive();
		// Criticality of agent should be updated after perception AND after action
		criticality = computeCriticality();
		criticalities.put(this, criticality);

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
		return String.format("Agent #%d", id);
	}
}
