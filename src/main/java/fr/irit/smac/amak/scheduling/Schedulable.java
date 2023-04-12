package fr.irit.smac.amak.scheduling;

/**
 * A schedulable object can be controlled by a scheduler
 */
public interface Schedulable {
	/**
	 * A cycle of the schedulable system
	 */
	void cycle() throws InterruptedException;

	/**
	 * Check if the schedulable must be stopped by the scheduler. For example, a
	 * stop condition can be "cycle==5000" aiming at stopping the system at the
	 * cycle 5000 in order to extract results or simply debugging.
	 *
	 * @return if the scheduler must stops its execution
	 */
	@SuppressWarnings("SameReturnValue")
	boolean stopCondition();

	/**
	 * This method is called when the scheduler starts
	 */
	@SuppressWarnings("EmptyMethod")
	void onSchedulingStarts();

	/**
	 * This method is called when the scheduler stops (by stopCondition or explicit
	 * stop)
	 */
	@SuppressWarnings("EmptyMethod")
	void onSchedulingStops();
}
