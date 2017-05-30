package fr.irit.smac.amak.examples.randomants;


import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Scheduling;

public class AntHill extends Amas<World> {

	public AntHill(World env) {
		super(env, Scheduling.MANUAL);
	}
	@Override
	protected void onInitialAgentsCreation() {
		for (int i=0;i<1000;i++)
			new Ant(this);
	}
}
