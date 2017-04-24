package fr.irit.smac.amak.examples.philosopherslinks;

import fr.irit.smac.amak.Environment;

public class TableEnvironment extends Environment {
	private Fork[] forks;

	@Override
	public void onInitialization() {
		// Set 10 forks on the table
		forks = new Fork[10];
		for (int i = 0; i < forks.length; i++)
			forks[i] = new Fork();
	}

	public Fork[] getForks() {
		return forks;
	}
}
