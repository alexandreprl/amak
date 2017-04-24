package fr.irit.smac.amak.examples.randomants;


import java.util.Random;

import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Scheduling;

public class AntHill extends Amas<World> {

	private Random random = new Random();
	public AntHill(World env) {
		super(env, Scheduling.MANUAL);
		new AntViewer(this);
	}
	public Random getRandom() {
		return random;
	}
	@Override
	protected void onInitialAgentsCreation() {
		for (int i=0;i<100;i++)
			new Ant(this);
		
	}
}
