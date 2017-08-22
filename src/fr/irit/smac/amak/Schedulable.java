package fr.irit.smac.amak;

/**
 * A schedulable object can be controlled by a scheduler
 * 
 * @author Alexandre Perles
 *
 */
public interface Schedulable {
	public int defaultSleep = 0;
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
}
