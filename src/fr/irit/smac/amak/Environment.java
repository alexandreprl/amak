package fr.irit.smac.amak;

import java.util.Random;

/**
 * This class must be overridden by environments
 * 
 * @author Alexandre Perles
 *
 */
public abstract class Environment {
	protected Object[] params;
	/**
	 * Random object common to the amas
	 */
	private Random random = new Random();

	/**
	 * Constructor
	 * 
	 * @param params
	 *            The parameters to initialize the environment
	 */
	public Environment(Object... params) {
		this.params = params;
		onInitialization();
		onInitialEntitiesCreation();
	}

	/**
	 * Set the seed for the common random object. This method should be called at
	 * the very beginning of the initialization process
	 * 
	 * @param _seed
	 *            The seed to initialize the random object
	 */
	public void setSeed(long _seed) {
		random = new Random(_seed);
	}

	/**
	 * This method is called during the initialization process of the environment
	 */
	public void onInitialization() {
	}
	/**
	 * This method is called after the initialization process of the environment to create entities
	 */
	public void onInitialEntitiesCreation() {
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

	/**
	 * Getter for the random object
	 * 
	 * @return the random object
	 */
	public Random getRandom() {
		return random;
	}
}
