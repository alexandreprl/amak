package fr.irit.smac.amak;

/**
 * A schedulable object can be controlled by a scheduler
 * 
 * @author Alexandre Perles
 *
 */
public interface Schedulable {
	/**
	 * A cycle of the schedulable system
	 */
	public void cycle();
}
