package fr.irit.smac.amak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JToolBar;

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
	 * Random object common to the amas
	 */
	private final Random random = new Random();

	/**
	 * The id of the amas
	 */
	private final int id = uniqueIndex++;
	private LinkedList<Agent<?, E>> agentsPendingRemoval = new LinkedList<>();
	private LinkedList<Agent<?, E>> agentsPendingAddition = new LinkedList<>();

	/**
	 * Constructor of the MAS
	 * 
	 * @param environment
	 *            Environment of the system
	 * @param scheduling
	 *            Scheduling mode
	 */
	public Amas(E environment, Scheduling scheduling) {
		this.environment = environment;
		this.onInitialConfiguration();
		this.onInitialAgentsCreation();
		
		for (Agent<?, E> agent : agentsPendingAddition) {
			agents.add(agent);
		}
		Agent<?, E> agent;
		while (!agentsPendingAddition.isEmpty()) {
			agent = agentsPendingAddition.poll();
			agent._onBeforeReady();
			agent.onReady();

		}
		this.onReady();
		this.scheduler = new Scheduler(this, Scheduling.hasAutostart(scheduling));
		if (Scheduling.isManual(scheduling))
			Toolbar.add(new SchedulerToolbar("Amas #" + id, getScheduler()));
	}

	/**
	 * This method is called at the very beginning of the amas creation. Any
	 * configuration should be made here.
	 */
	protected void onInitialConfiguration() {
	}

	/**
	 * This method is called when all agents are ready
	 */
	protected void onReady() {
	}

	/**
	 * Getter for the random object
	 * 
	 * @return the random object
	 */
	public Random getRandom() {
		return random;
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
	 *            the agent to add to the system
	 */
	public final void _addAgent(Agent<?, E> _agent) {
		agentsPendingAddition.add(_agent);
	}

	/**
	 * Remove an agent from the MAS
	 * 
	 * @param _agent
	 *            the agent to remove from the system
	 */
	public final void _removeAgent(Agent<?, E> _agent) {
		agentsPendingRemoval.add(_agent);
	}

	/**
	 * Cycle of the system
	 */
	public final void cycle() {
		cycle++;
		getEnvironment().onCycleBegin();
		Collections.sort(agents, new AgentOrderComparator());
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
		while (!agentsPendingRemoval.isEmpty())
			agents.remove(agentsPendingRemoval.poll());
		for (Agent<?, E> agent : agentsPendingAddition) {
			agents.add(agent);
		}
		Agent<?, E> agent2;
		while (!agentsPendingAddition.isEmpty()) {
			agent2 = agentsPendingAddition.poll();
			agent2._onBeforeReady();
			agent2.onReady();

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

	private class AgentOrderComparator implements Comparator<Agent> {

		@Override
		public int compare(Agent o1, Agent o2) {
			if (o1.getExecutionOrder() == o2.getExecutionOrder())
				return 0;
			else
				return o1.getExecutionOrder() - o2.getExecutionOrder();
		}

	}
}
