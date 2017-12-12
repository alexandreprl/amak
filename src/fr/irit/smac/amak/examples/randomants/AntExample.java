package fr.irit.smac.amak.examples.randomants;

import fr.irit.smac.amak.Agent;

public class AntExample extends Agent<AntHillExample, WorldExample> {
	/**
	 * X coordinate of the ant in the world
	 */
	public double dx = 0;
	/**
	 * Y coordinate of the ant in the world
	 */
	public double dy = 0;
	/**
	 * Angle in radians
	 */
	private double angle = Math.random()*Math.PI*2;

	/**
	 * Constructor of the ant
	 * 
	 * @param amas
	 *            the amas the ant belongs to
	 */
	public AntExample(AntHillExample amas) {
		super(amas);
	}

	/**
	 * Move in a random direction
	 */
	@Override
	protected void onAct() {
		double random = amas.getEnvironment().getRandom().nextGaussian();
		angle += random * 0.1;
		dx += Math.cos(angle);
		dy += Math.sin(angle);
		while (dx >= getAmas().getEnvironment().getWidth()/2)
			dx -= getAmas().getEnvironment().getWidth();
		while (dy >= getAmas().getEnvironment().getHeight()/2)
			dy -= getAmas().getEnvironment().getHeight();
		while (dx < -getAmas().getEnvironment().getWidth()/2)
			dx += getAmas().getEnvironment().getWidth();
		while (dy < -getAmas().getEnvironment().getHeight()/2)
			dy += getAmas().getEnvironment().getHeight();

	}
}