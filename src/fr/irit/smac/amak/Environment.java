package fr.irit.smac.amak;

public abstract class Environment {
	public Environment() {
		onInitialization();
	}
	public void onInitialization() {
	}
	public void onCycleBegin() {
	}
	public void onCycleEnd(Amas<? extends Environment> amas) {
	}
}
