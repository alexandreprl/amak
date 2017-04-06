package fr.irit.smac.amak;

/**
 * This class must be overridden by environments
 * 
 * @author Alexandre Perles
 *
 */
public abstract class Environment {
	/**
	 * Constructor
	 */
	public Environment() {
		onInitialization();
	}
	/**
	 * This method is called during the initialization process of the environment
	 */
	public void onInitialization() {
	}
	/**
	 * This method is called before each cycle of the system
	 */
	public void onCycleBegin() {
	}
	/**
	 * This method is called after each cycle of the system
	 */
	public void onCycleEnd() {
	}
}
