package fr.irit.smac.amak.examples.randomants;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.ui.VUI;
import fr.irit.smac.amak.ui.drawables.DrawablePoint;

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
	private double angle = Math.random() * Math.PI * 2;
	private DrawablePoint drawablePoint;

	/**
	 * Constructor of the ant
	 * 
	 * @param amas
	 *            the amas the ant belongs to
	 */
	public AntExample(AntHillExample amas) {
		super(amas);
	}

	@Override
	protected void onReady() {
		dx = 0;
		dy = 0;
		drawablePoint = VUI.get().createPoint(dx, dy);
		drawablePoint.setStrokeOnly();
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
		drawablePoint.move(dx, dy);
	}
}
