package fr.irit.smac.amak.examples.randomants;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.ui.VUI;
import fr.irit.smac.amak.ui.drawables.DrawableImage;

public class AntExample extends Agent<AntHillExample, WorldExample> {
	/**
	 * X coordinate of the ant in the world
	 */
	public double dx;
	/**
	 * Y coordinate of the ant in the world
	 */
	public double dy;
	/**
	 * Angle in radians
	 */
	private double angle = Math.random() * Math.PI * 2;
	private DrawableImage image;

	/**
	 * Constructor of the ant
	 * 
	 * @param amas
	 *            the amas the ant belongs to
	 * @param startX
	 *            Initial X coordinate
	 * @param startY
	 *            Initial Y coordinate
	 */
	public AntExample(AntHillExample amas, double startX, double startY) {
		super(amas, startX, startY);
	}
	@Override
	protected void onInitialization() {
		dx = (double) params[0];
		dy = (double) params[1];
	}

	@Override
	protected void onRenderingInitialization() {
		image = VUI.get().createImage(dx, dy, "Resources/ant.png");
	}

	/**
	 * Move in a random direction
	 */
	@Override
	protected void onDecideAndAct() {
		double random = amas.getEnvironment().getRandom().nextGaussian();
		angle += random * 0.1;
		dx += Math.cos(angle);
		dy += Math.sin(angle);
		while (dx >= getAmas().getEnvironment().getWidth() / 2)
			dx -= getAmas().getEnvironment().getWidth();
		while (dy >= getAmas().getEnvironment().getHeight() / 2)
			dy -= getAmas().getEnvironment().getHeight();
		while (dx < -getAmas().getEnvironment().getWidth() / 2)
			dx += getAmas().getEnvironment().getWidth();
		while (dy < -getAmas().getEnvironment().getHeight() / 2)
			dy += getAmas().getEnvironment().getHeight();

		if (amas.getEnvironment().getRandom().nextDouble() < 0.001) {
			image.setFilename("Resources/ant_dead.png");
			destroy();
		}

		if (amas.getEnvironment().getRandom().nextDouble() < 0.001) {
			new AntExample(getAmas(), dx, dy);
		}
	}

	@Override
	protected void onUpdateRender() {
		image.move(dx, dy);
		image.setAngle(angle);
	}
}
