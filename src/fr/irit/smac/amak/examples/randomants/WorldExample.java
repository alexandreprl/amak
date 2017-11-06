package fr.irit.smac.amak.examples.randomants;

import fr.irit.smac.amak.Environment;

public class WorldExample extends Environment {
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
