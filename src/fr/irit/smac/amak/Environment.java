package fr.irit.smac.amak;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import fr.irit.smac.amak.ui.MainWindow;
import fr.irit.smac.amak.ui.SchedulerToolbar;

/**
 * This class must be overridden by environments
 * 
 * @author Alexandre Perles
 *
 */
public abstract class Environment implements Schedulable {

	/**
	 * Unique index to give unique id to each environment
	 */
	private static int uniqueIndex;

	/**
	 * The id of the environment
	 */
	private final int id = uniqueIndex++;
	protected Object[] params;
	/**
	 * Random object common to the amas
	 */
	private Random random = new Random();
	private Scheduler scheduler;

	/**
	 * Constructor
	 * 
	 * @param _scheduling
	 *            The scheduling of the environment
	 * @param params
	 *            The parameters to initialize the environment
	 */
	public Environment(Scheduling _scheduling, Object... params) {
		this.params = params;
		onInitialization();
		onInitialEntitiesCreation();

		if (_scheduling == Scheduling.DEFAULT) {
			this.scheduler = Scheduler.getDefaultScheduler();
			this.scheduler.add(this);
		} else {
			this.scheduler = new Scheduler(this);
			if (_scheduling == Scheduling.UI)
				MainWindow.addToolbar(new SchedulerToolbar("Environment #" + id, getScheduler()));
		}
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
	 * This method is called after the initialization process of the environment to
	 * create entities
	 */
	public void onInitialEntitiesCreation() {
	}

	/**
	 * This method is called at each cycle of the environment
	 */
	public void onCycle() {
	}

	@Override
	public boolean stopCondition() {
		return false;
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
}
