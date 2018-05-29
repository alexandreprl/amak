package fr.irit.smac.amak;

/**
 * This class can be used to create a fully asynchronous agent. This agent must
 * not be connected to any MAS.
 * 
 * @author perles
 *
 * @param <E>
 *            The environment the agent is connected to.
 */
public class AsyncAgent<E extends Environment> extends Agent<AsyncAmas<E>, E> implements Schedulable {

	private Scheduler scheduler;
	private E getEnvironment;

	/**
	 * Create an async agent
	 * 
	 * @param environment
	 *            The environment of the agent
	 * @param sleep
	 *            The time in milliseconds the agent has to wait between each cycle
	 * @param params
	 *            Optional parameters
	 */
	public AsyncAgent(E environment, int sleep, Object... params) {
		super(null, params);
		this.getEnvironment = environment;
		this.scheduler = new Scheduler(this);
		this.scheduler.startWithSleep(sleep);
	}

	@Override
	public void cycle() {
		currentPhase = Phase.PERCEPTION;
		phase1();
		currentPhase = Phase.DECISION_AND_ACTION;
		phase2();
	}

	@Override
	public void destroy() {
		this.scheduler.stop();
	}

	@Override
	public void onSchedulingStarts() {
	}

	@Override
	public void onSchedulingStops() {
	}

	@Override
	public boolean stopCondition() {
		return false;
	}

	@Override
	protected int _computeExecutionOrder() {
		return computeExecutionOrderLayer();
	}

	@Override
	public E getEnvironment() {
		return getEnvironment;
	}
}
