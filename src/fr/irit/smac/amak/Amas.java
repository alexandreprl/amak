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
public class Amas<E extends Environment> {
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
	 * The scheduling of a system can be manual or automatic
	 */
	public enum Scheduling {
		MANUAL, AUTO
	}

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
			Toolbar.add(new SchedulerToolbar(getScheduler()));
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	protected void onInitialAgentsCreation() {
	}

	public final void _addAgent(Agent<?, E> _agent) {
		agents.add(_agent);
	}

	public final void cycle() {
		cycle++;
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
	}

	protected void onSystemCycleEnd() {

	}

	protected void onSystemCycleBegin() {

	}

	public final int getCycle() {
		return cycle;
	}

	public final E getEnvironment() {
		return environment;
	}

	public boolean stopCondition() {
		return false;
	}

	public List<Agent<?, E>> getAgents() {
		return agents;
	}
}
