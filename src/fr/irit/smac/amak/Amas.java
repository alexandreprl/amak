package fr.irit.smac.amak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.irit.smac.amak.ui.SchedulerToolbar;
import fr.irit.smac.amak.ui.Toolbar;

/**
 * This class must be overridden by multi-agent systems
 * 
 * @author Alexandre Perles
 *
 * @param <E>
 *            The environment of the MAS
 */
public class Amas<E extends Environment> implements Schedulable {
	/**
	 * List of agents present in the system
	 */
	protected final List<Agent<?, E>> agents = new ArrayList<>();
	/**
	 * Number of cycles executed by the system
	 */
	protected int cycle;
	/**
	 * Environment of the system
	 */
	protected final E environment;
	/**
	 * Scheduler controlling the execution of the system
	 */
	private final Scheduler scheduler;

	/**
	 * Unique index to give unique id to each amas
	 */
	private static int uniqueIndex;

	/**
	 * The id of the amas
	 */
	private final int id = uniqueIndex++;

	/**
	 * Constructor of the MAS
	 * 
	 * @param env
	 *            Environment of the system
	 * @param scheduling
	 *            Scheduling mode
	 */
	public Amas(E env, Scheduling scheduling) {
		this.environment = env;
		this.onInitialAgentsCreation();
		for (Agent<?, E> agent : agents) {
			agent.onReady();
		}
		this.scheduler = new Scheduler(this, scheduling == Scheduling.AUTO);
		if (scheduling == Scheduling.MANUAL)
			Toolbar.add(new SchedulerToolbar("Amas #" + id, getScheduler()));
	}

	/**
	 * Getter for the scheduler
	 * 
	 * @return the scheduler
	 */
	public Scheduler getScheduler() {
		return scheduler;
	}

	/**
	 * This method should be overridden, the agents should be created in this
	 * method
	 */
	protected void onInitialAgentsCreation() {
	}

	/**
	 * Add an agent to the MAS. This method is called by the agent itself during
	 * its creation
	 * 
	 * @param _agent
	 *            the agent to addto the system
	 */
	public final void _addAgent(Agent<?, E> _agent) {
		// TODO add the agent at the end of a cycle (pending add agent)
		// TODO call initialization
		// TODO same on destroy
		agents.add(_agent);
	}

	/**
	 * Cycle of the system
	 */
	public final void cycle() {
		cycle++;
		getEnvironment().onCycleBegin();
		Collections.shuffle(agents);
		onSystemCycleBegin();
		for (Agent<?, E> agent : agents) {
			agent.onSystemCycleBegin();
		}
		for (Agent<?, E> agent : agents) {
			agent.cycle();
		}
		for (Agent<?, E> agent : agents) {
			agent.onSystemCycleEnd();
		}
		onSystemCycleEnd();
		getEnvironment().onCycleEnd();
	}

	/**
	 * This method is called when all agents have executed a cycle
	 */
	protected void onSystemCycleEnd() {

	}

	/**
	 * This method is called before all agents have executed a cycle
	 */
	protected void onSystemCycleBegin() {

	}

	/**
	 * Getter for the current cycle number
	 * 
	 * @return the current cycle
	 */
	public final int getCycle() {
		return cycle;
	}

	/**
	 * Getter for the environment
	 * 
	 * @return the environment
	 */
	public final E getEnvironment() {
		return environment;
	}

	/**
	 * This method allows the system to stop the scheduler on certain conditions
	 * 
	 * @return whether or not the scheduler must stop.
	 */
	public boolean stopCondition() {
		return false;
	}

	/**
	 * Getter for the list of agents
	 * 
	 * @return the list of agents
	 */
	public List<Agent<?, E>> getAgents() {
		return agents;
	}
}
