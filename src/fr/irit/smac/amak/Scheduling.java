package fr.irit.smac.amak;

/**
 * The scheduling of a system can be controlled with UI or hidden and controlled
 * with the code.
 * 
 * Example: amas.getScheduler().start();
 */
public enum Scheduling {
	/**
	 * Create a new scheduler and show it as a toolbar
	 */
	UI,
	/**
	 * Create a new scheduler and hide it
	 */
	HIDDEN,
	/**
	 * Use the default scheduler
	 */
	DEFAULT
}