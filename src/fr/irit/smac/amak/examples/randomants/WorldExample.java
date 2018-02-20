package fr.irit.smac.amak.examples.randomants;

import fr.irit.smac.amak.Environment;
import fr.irit.smac.amak.Scheduling;

public class WorldExample extends Environment {
	public WorldExample(Object...params) {
		super(Scheduling.DEFAULT, params);
	}

	private int width;
	private int height;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public void onInitialization() {
		this.width = 800;
		this.height = 600;
	}

}
