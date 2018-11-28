package fr.irit.smac.amak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import fr.irit.smac.amak.ui.MainWindow;
import fr.irit.smac.amak.ui.SchedulerToolbar;
import fr.irit.smac.amak.ui.VUI;

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
	protected final List<Agent<? extends Amas<E>, E>> agents = new ArrayList<>();
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
	private Scheduler scheduler;

	/**
	 * Unique index to give unique id to each amas
	 */
	private static int uniqueIndex;

	/**
	 * The id of the amas
	 */
	private final int id = uniqueIndex++;
	/**
	 * Agents that must be removed from the AMAS at the end of the cycle
	 */
	private LinkedList<Agent<?, E>> agentsPendingRemoval = new LinkedList<>();
	/**
	 * Agents that must be added to the AMAS at the end of the cycle
	 */
	private LinkedList<Agent<?, E>> agentsPendingAddition = new LinkedList<>();
	/**
	 * Parameters that can be passed to the constructor. These parameters are meant
	 * to be used only in the method onInitialConfiguration.
	 */
	protected Object[] params;
	/**
	 * This thread executor is here to run the agent cycle.
	 */
	private ThreadPoolExecutor executor;
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
	 * The executionPolicy informs if agents must wait each other after the
	 * perception and the decisionAndSction phases or only after the
	 * decisionAndCycle phase.
	 */
	public enum ExecutionPolicy {
		/**
		 * Every agent perceives, then every agent agent decides and acts
		 */
		TWO_PHASES,
		/**
		 * Every agent perceives, decides and act. When they all have finished, they
		 * start again (or die)
		 */
		ONE_PHASE
	}

	/**
	 * The execution policy {@link ExecutionPolicy}
	 */
	private ExecutionPolicy executionPolicy;
	private List<Agent<?, ?>> runningAsyncAgents = new ArrayList<>();

	/**
	 * Getter for the execution policy
	 * 
	 * @return the current execution policy
	 */
	public ExecutionPolicy getExecutionPolicy() {
		return executionPolicy;
	}

	/**
	 * Constructor of the MAS
	 * 
	 * @param environment
	 *            Environment of the system
	 * @param scheduling
	 *            Scheduling mode
	 * @param params
	 *            The params to initialize the amas
	 */
	public Amas(E environment, Scheduling scheduling, Object... params) {
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Configuration.allowedSimultaneousAgentsExecution);
		if (scheduling == Scheduling.DEFAULT) {
			this.scheduler = Scheduler.getDefaultScheduler();
			this.scheduler.add(this);
		} else {
			this.scheduler = new Scheduler(this);
			if (scheduling == Scheduling.UI && !Configuration.commandLineMode)
				MainWindow.addToolbar(new SchedulerToolbar("Amas #" + id, getScheduler()));
		}
		this.scheduler.lock();
		this.params = params;
		this.environment = environment;
		this.onInitialConfiguration();
		executionPolicy = Configuration.executionPolicy;
		this.onInitialAgentsCreation();

		addPendingAgents();
		this.onReady();
		if (!Configuration.commandLineMode)
			this.onRenderingInitialization();
		this.scheduler.unlock();
	}

	/**
	 * The method in which the rendering initialization should be made. For example,
	 * the creation of a VUI object {@link VUI}
	 */
	protected void onRenderingInitialization() {
	}

	/**
	 * Effectively add agent to the system
	 */
	private void addPendingAgents() {
		// The double loop is required as the method onReady should only be called when
		// all the agents have been added
		synchronized (agentsPendingAddition) {

			for (Agent<?, E> agent : agentsPendingAddition) {
				agents.add(agent);
			}
			while (!agentsPendingAddition.isEmpty()) {
				final Agent<?, E> agent = agentsPendingAddition.poll();
				agent._onBeforeReady();
				agent.onReady();
				if (!agent.isSynchronous()) {
					scheduler.addOnChange(s -> {
						if (s.isRunning() && !runningAsyncAgents.contains(agent)) {
							startRunningAsyncAgent(agent);
						}
					});
					startRunningAsyncAgent(agent);
				}
			}
		}
	}

	private void startRunningAsyncAgent(Agent<?, E> agent) {
		runningAsyncAgents.add(agent);
		runAsynchronousAgent(agent);
	}

	private void runAsynchronousAgent(Agent<?, E> agent) {
		executor.execute(() -> {
			agent.onePhaseCycle();
			if (scheduler.isRunning() && agents.contains(agent)) {
				try {
					Thread.sleep(scheduler.getSleep());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runAsynchronousAgent(agent);
			} else {
				runningAsyncAgents.remove(agent);
			}
		});
	}

	/**
	 * Inform that agent gas finished the perception phase.
	 */
	protected final void informThatAgentPerceptionIsFinished() {
		perceptionPhaseSemaphore.release();
	}

	/**
	 * Inform that agent has finished the DecisionAndAction phase
	 */
	protected final void informThatAgentDecisionAndActionAreFinished() {
		decisionAndActionPhasesSemaphore.release();
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
	 * Getter for the scheduler
	 * 
	 * @return the scheduler
	 */
	public Scheduler getScheduler() {
		return scheduler;
	}

	/**
	 * This method should be overridden, the agents should be created in this method
	 */
	protected void onInitialAgentsCreation() {
	}

	/**
	 * Add an agent to the MAS. This method is called by the agent itself during its
	 * creation
	 * 
	 * @param _agent
	 *            the agent to add to the system
	 */
	public final void _addAgent(Agent<?, E> _agent) {
		synchronized (agentsPendingAddition) {
			agentsPendingAddition.add(_agent);
		}
	}

	/**
	 * Remove an agent from the MAS
	 * 
	 * @param _agent
	 *            the agent to remove from the system
	 */
	public final void _removeAgent(Agent<?, E> _agent) {
		synchronized (agentsPendingRemoval) {
			agentsPendingRemoval.add(_agent);
		}
	}

	/**
	 * Cycle of the system
	 */
	public final void cycle() {
		cycle++;
		List<Agent<? extends Amas<E>, E>> synchronousAgents = agents.stream().filter(a -> a.isSynchronous())
				.collect(Collectors.toList());
		Collections.sort(synchronousAgents, new AgentOrderComparator());
		onSystemCycleBegin();

		switch (executionPolicy) {
		case ONE_PHASE:
			for (Agent<?, E> agent : synchronousAgents) {
				executor.execute(agent);
			}
			try {
				perceptionPhaseSemaphore.acquire(synchronousAgents.size());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				decisionAndActionPhasesSemaphore.acquire(synchronousAgents.size());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case TWO_PHASES:
			// Perception
			for (Agent<?, E> agent : synchronousAgents) {
				executor.execute(agent);
			}
			try {
				perceptionPhaseSemaphore.acquire(synchronousAgents.size());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Decision and action
			for (Agent<?, E> agent : synchronousAgents) {
				executor.execute(agent);
			}
			try {
				decisionAndActionPhasesSemaphore.acquire(synchronousAgents.size());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}

		removePendingAgents();

		addPendingAgents();

		onSystemCycleEnd();
		if (!Configuration.commandLineMode)

			onUpdateRender();

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
	 * The method in which you can update the rendering. For example, update the
	 * color of the VUI drawable object create in
	 * {@link Amas#onRenderingInitialization}
	 */
	protected void onUpdateRender() {
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
	@Override
	public boolean stopCondition() {
		return false;
	}

	/**
	 * Getter for the list of agents
	 * 
	 * @return the list of agents
	 */
	public List<Agent<? extends Amas<E>, E>> getAgents() {
		return agents;
	}

	/**
	 * Comparator to sort agents for execution
	 * 
	 * @author Alexandre Perles
	 *
	 */
	private class AgentOrderComparator implements Comparator<Agent<? extends Amas<E>, E>> {

		@Override
		public int compare(Agent<? extends Amas<E>, E> o1, Agent<? extends Amas<E>, E> o2) {
			if (o1.getExecutionOrder() == o2.getExecutionOrder())
				return 0;
			else
				return o1.getExecutionOrder() - o2.getExecutionOrder();
		}

	}

	/**
	 * Helper method to launch the scheduler
	 */
	public void start() {
		getScheduler().start();
	}

	/**
	 * When the scheduling starts we need to create the thread pool for agents
	 * execution
	 */
	@Override
	public final void onSchedulingStarts() {
		
	}

	@Override
	public final void onSchedulingStops() {

	}

	/**
	 * Getter for the decisionAndActionPhasesSemaphore
	 * 
	 * @return the decisionAndActionPhasesSemaphore
	 */
	public Semaphore getDecisionAndActionPhasesSemaphore() {
		return decisionAndActionPhasesSemaphore;
	}

	/**
	 * Getter for the perceptionPhaseSemaphore
	 * 
	 * @return the perceptionPhaseSemaphore
	 */
	public Semaphore getPerceptionPhaseSemaphore() {
		return perceptionPhaseSemaphore;
	}
}
