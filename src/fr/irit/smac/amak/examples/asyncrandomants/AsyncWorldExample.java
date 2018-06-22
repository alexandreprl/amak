package fr.irit.smac.amak.examples.asyncrandomants;

import fr.irit.smac.amak.Environment;
import fr.irit.smac.amak.Scheduling;

/**
 * The world in which the ants evolve
 * 
 * @author perles
 *
 */
public class AsyncWorldExample extends Environment {

	/**
	 * Required world constructor
	 */
	public AsyncWorldExample() {
		super(Scheduling.HIDDEN);
	}

	/**
	 * The width of the world the ants can evolve in
	 */
	private int width;
	/**
	 * The height of the world the ants can evolve in
	 */
	private int height;

	/**
	 * Width getter
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Height getter
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	@Override
	public void onInitialization() {
		this.width = 800;
		this.height = 600;
	}

}
