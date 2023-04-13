package fr.irit.smac.amak;

import fr.irit.smac.amak.scheduling.Schedulable;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * This class must be overridden by multi-agent systems
 *
 * @param <E> The environment of the MAS
 */
public class Amas<E extends Environment> implements Schedulable {
	/**
	 * List of agents present in the system
	 */
	protected final List<Agent<? extends Amas<E>, E>> agents = new ArrayList<>();
	/**
	 * Environment of the system
	 */
	@Getter
	protected final E environment;
	/**
	 * Parameters that can be passed to the constructor. These parameters are meant
	 * to be used only in the method onInitialConfiguration.
	 */
	protected final Object[] params;
	/**
	 * Agents that must be removed from the AMAS at the end of the cycle
	 */
	private final LinkedList<Agent<?, E>> agentsPendingRemoval = new LinkedList<>();
	/**
	 * Agents that must be added to the AMAS at the end of the cycle
	 */
	private final LinkedList<Agent<?, E>> agentsPendingAddition = new LinkedList<>();
	/**
	 * This thread executor is here to run the agent cycle.
	 */
	private final ThreadPoolExecutor executor;
	/**
	 * This semaphore is meant to synchronize the agents after the decisionAndAction
	 * phase
	 */
	private final Semaphore decisionAndActionPhasesSemaphore = new Semaphore(0);
	/**
	 * This semaphore is meant to synchronize the agents after the perception phase
	 */
	private final Semaphore perceptionPhaseSemaphore = new Semaphore(0);
	/**
	 * The execution policy {@link ExecutionPolicy}
	 */
	private final ExecutionPolicy executionPolicy;
	/**
	 * Number of cycles executed by the system
	 */
	protected int cycle;

	/**
	 * Constructor of the MAS
	 *
	 * @param environment Environment of the system
	 * @param params      The params to initialize the amas
	 */
	public Amas(E environment, int allowedSimultaneousAgentsExecution, ExecutionPolicy executionPolicy, Object... params) {
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(allowedSimultaneousAgentsExecution);
		this.params = params;
		this.environment = environment;
		this.onInitialConfiguration();
		this.executionPolicy = executionPolicy;
		this.onInitialAgentsCreation();

		addPendingAgents();
		this.onReady();
	}

	/**
	 * Effectively add agent to the system
	 */
	private void addPendingAgents() {
		// The double loop is required as the method onReady should only be called when
		// all the agents have been added
		synchronized (agentsPendingAddition) {

			agents.addAll(agentsPendingAddition);
			while (!agentsPendingAddition.isEmpty()) {
				final Agent<?, E> agent = agentsPendingAddition.poll();
				agent.onBeforeReady();
				agent.onReady();
			}
		}
	}

	/**
	 * Add an agent to the MAS. This method is called by the agent itself during its
	 * creation
	 *
	 * @param agent the agent to add to the system
	 */
	final void addAgent(Agent<?, E> agent) {
		synchronized (agentsPendingAddition) {
			agentsPendingAddition.add(agent);
		}
	}

	/**
	 * Remove an agent from the MAS
	 *
	 * @param agent the agent to remove from the system
	 */
	final void removeAgent(Agent<?, E> agent) {
		synchronized (agentsPendingRemoval) {
			agentsPendingRemoval.add(agent);
		}
	}

	/**
	 * Cycle of the system
	 */
	public final void cycle() throws InterruptedException {
		cycle++;
		onSystemCycleBegin();

		if (Objects.requireNonNull(executionPolicy) == ExecutionPolicy.ONE_PHASE) {
			for (Agent<?, E> agent : agents) {
				executor.execute(()->{
					agent.cycle();
					perceptionPhaseSemaphore.release();
					decisionAndActionPhasesSemaphore.release();
				});
			}
			perceptionPhaseSemaphore.acquire(agents.size());
			decisionAndActionPhasesSemaphore.acquire(agents.size());
		} else if (executionPolicy == ExecutionPolicy.TWO_PHASES) {
			for (Agent<?, E> agent : agents) {
				executor.execute(()->{
					agent.phase1();
					perceptionPhaseSemaphore.release();
				});
			}
			perceptionPhaseSemaphore.acquire(agents.size());
			for (Agent<?, E> agent : agents) {
				executor.execute(()->{
					agent.phase2();
					decisionAndActionPhasesSemaphore.release();
				});
			}
			decisionAndActionPhasesSemaphore.acquire(agents.size());
		}

		removePendingAgents();

		addPendingAgents();

		onSystemCycleEnd();

	}

	/**
	 * Effectively remove the agents that has been destroyed in the previous cycle
	 * to avoid {@link ConcurrentModificationException}
	 */
	private void removePendingAgents() {

		synchronized (agentsPendingRemoval) {
			while (!agentsPendingRemoval.isEmpty())
				agents.remove(agentsPendingRemoval.poll());
		}
	}

	/**
	 * This method is called at the very beginning of the AMAS creation. Any
	 * configuration should be made here.
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onInitialConfiguration() {
		// To be implemented
	}

	/**
	 * This method is called when all agents are ready
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onReady() {
		// To be implemented
	}

	/**
	 * This method should be overridden, the agents should be created in this method
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onInitialAgentsCreation() {
		// To be implemented
	}

	/**
	 * This method is called when all agents have executed a cycle
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onSystemCycleEnd() {
		// To be implemented
	}

	/**
	 * This method is called before all agents have executed a cycle
	 */
	@SuppressWarnings("EmptyMethod")
	protected void onSystemCycleBegin() {
		// To be implemented
	}

	/**
	 * This method allows the system to stop the scheduler on certain conditions
	 *
	 * @return whether the scheduler must stop.
	 */
	@Override
	public boolean stopCondition() {
		return false;
	}

	@Override
	public void onSchedulingStarts() {
		// To be implemented
	}

	@Override
	public void onSchedulingStops() {
		// To be implemented
	}

	/**
	 * The executionPolicy informs if agents must wait each other after the
	 * perception and the decisionAndAction phases or only after the
	 * decisionAndCycle phase.
	 */
	public enum ExecutionPolicy {
		/**
		 * Every agent perceives, then every agent decides and acts
		 */
		TWO_PHASES,
		/**
		 * Every agent perceives, decides and act. When they all have finished, they
		 * start again (or die)
		 */
		ONE_PHASE
	}
}
