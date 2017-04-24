package fr.irit.smac.amak.examples.randomants;

import fr.irit.smac.amak.Agent;

public class Ant extends Agent<AntHill, World> {
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
	public Ant(AntHill amas) {
		super(amas);
	}

	/**
	 * Move in a random direction
	 */
	@Override
	protected void onAct() {
		double random = amas.getRandom().nextGaussian();
		angle += random * 0.1;
		dx += Math.cos(angle);
		dy += Math.sin(angle);
		while (dx >= 400)
			dx -= 800;
		while (dy >= 300)
			dy -= 600;
		while (dx < -400)
			dx += 800;
		while (dy < -300)
			dy += 600;

	}
}
