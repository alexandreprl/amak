package fr.irit.smac.amak.runner;

import fr.irit.smac.amak.Amas;

public abstract class Runner {
	protected Amas<?> amas;

	public Runner(Amas<?> amas) {
		this.amas = amas;
	}
	public void cycle() {
		amas.getEnvironment().onCycleBegin();
		amas.cycle();
		amas.getEnvironment().onCycleEnd(amas);
		
	}
}
