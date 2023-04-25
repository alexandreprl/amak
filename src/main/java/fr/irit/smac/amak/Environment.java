package fr.irit.smac.amak;

import fr.irit.smac.amak.scheduling.Schedulable;

import java.util.Random;

/**
 * This class must be overridden by environments
 */
public abstract class Environment implements Schedulable {
	/**
	 * Random object common to the amas
	 */
	private Random random = new Random();

	/**
	 * Constructor
	 *
	 * @param params The parameters to initialize the environment
	 */
	protected Environment() {
		onInitialization();
		onInitialEntitiesCreation();
	}

	/**
	 * Set the seed for the common random object. This method should be called at
	 * the very beginning of the initialization process
	 *
	 * @param seed The seed to initialize the random object
	 */
	public void setSeed(long seed) {
		random = new Random(seed);
	}

	@Override
	public final void cycle() {
		onCycle();
	}

	/**
	 * Getter for the random object
	 *
	 * @return the random object
	 */
	public Random getRandom() {
		return random;
	}

	@Override
	public void onSchedulingStarts() {
	}

	@Override
	public void onSchedulingStops() {
	}

	/**
	 * This method is called during the initialization process of the environment
	 */
	@SuppressWarnings("EmptyMethod")
	public void onInitialization() {
	}

	/**
	 * This method is called after the initialization process of the environment to
	 * create entities
	 */
	@SuppressWarnings("EmptyMethod")
	public void onInitialEntitiesCreation() {
	}

	/**
	 * This method is called at each cycle of the environment
	 */
	@SuppressWarnings("EmptyMethod")
	public void onCycle() {
	}

	@Override
	public boolean stopCondition() {
		return false;
	}
}
