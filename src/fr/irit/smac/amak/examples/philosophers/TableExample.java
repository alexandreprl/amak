package fr.irit.smac.amak.examples.philosophers;

import fr.irit.smac.amak.Environment;

public class TableExample extends Environment {
	private ForkExample[] forks;

	@Override
	public void onInitialization() {
		// Set 10 forks on the table
		forks = new ForkExample[10];
		for (int i = 0; i < forks.length; i++)
			forks[i] = new ForkExample();
	}

	public ForkExample[] getForks() {
		return forks;
	}
}