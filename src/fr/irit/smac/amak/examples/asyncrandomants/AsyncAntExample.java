package fr.irit.smac.amak.examples.asyncrandomants;

import java.util.Random;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.ui.VUI;
import fr.irit.smac.amak.ui.drawables.DrawableImage;

/**
 * This class is an example of a completely asynchronous agent
 * 
 * @author perles
 *
 */
public class AsyncAntExample extends Agent<AsyncAntsAMASExample, AsyncWorldExample> {
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
	/**
	 * The image that will be used to render the ant
	 */
	private DrawableImage image;
	/**
	 * Random object made for randomize the movement
	 */
	private Random randomObject;

	/**
	 * Constructor of the ant
	 * 
	 * @param env
	 *            The environment of the agent
	 * @param startX
	 *            Initial X coordinate
	 * @param startY
	 *            Initial Y coordinate
	 */
	public AsyncAntExample(AsyncAntsAMASExample amas, double startX, double startY) {
		super(amas, startX, startY);
	}

	@Override
	protected void onInitialization() {
		dx = (double) params[0];
		dy = (double) params[1];
		randomObject = new Random();
		setAsynchronous();
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
		double random = randomObject.nextGaussian();
		angle += random * 0.1;
		dx += Math.cos(angle);
		dy += Math.sin(angle);
		while (dx >= this.getEnvironment().getWidth() / 2)
			dx -= getEnvironment().getWidth();
		while (dy >= getEnvironment().getHeight() / 2)
			dy -= getEnvironment().getHeight();
		while (dx < -getEnvironment().getWidth() / 2)
			dx += getEnvironment().getWidth();
		while (dy < -getEnvironment().getHeight() / 2)
			dy += getEnvironment().getHeight();

		if (getEnvironment().getRandom().nextDouble() < 0.01) {
			image.setFilename("Resources/ant_dead.png");
			destroy();
		}

		if (getEnvironment().getRandom().nextDouble() < 0.01) {
			new AsyncAntExample(getAmas(), dx, dy);
		}
	}

	@Override
	protected void onUpdateRender() {
		image.move(dx, dy);
		image.setAngle(angle);
	}
}
