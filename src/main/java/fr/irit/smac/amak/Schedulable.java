package fr.irit.smac.amak;

/**
 * A schedulable object can be controlled by a scheduler
 * 
 * @author Alexandre Perles
 *
 */
public interface Schedulable {
	/**
	 * The default time between scheduler cycle
	 */
	public static final int DEFAULT_SLEEP = 0;

	/**
	 * A cycle of the schedulable system
	 */
	public void cycle();

	/**
	 * Check if the schedulable must be stopped by the scheduler. For example, a
	 * stop condition can be "cycle==5000" aiming at stopping the system at the
	 * cycle 5000 in order to extract results or simply debugging.
	 * 
	 * @return if the scheduler must stops its execution
	 */
	public boolean stopCondition();

	/**
	 * This method is called when the scheduler starts
	 */
	public void onSchedulingStarts();

	/**
	 * This method is called when the scheduler stops (by stopCondition or explicit
	 * stop)
	 */
	public void onSchedulingStops();
}
