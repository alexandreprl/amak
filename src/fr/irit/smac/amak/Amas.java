package fr.irit.smac.amak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Amas<T extends Environment> {

	protected List<Agent<? extends Amas<T>>> agents = new ArrayList<>();
	protected int cycle;
	protected T environment;

	public Amas(T env) {
		this.environment = env;
		this.onInitialAgentsCreation();
		for (Agent<? extends Amas<T>> agent : agents) {
			agent.onReady();
		}
	}
	protected void onInitialAgentsCreation() {
	}
	public final void addAgent(Agent<? extends Amas<T>> _agent) {
		agents.add(_agent);
	}
	public final void cycle() {
		cycle++;
		onCycleBegin();
		Collections.shuffle(agents);
		for (Agent<? extends Amas<T>> agent : agents) {
			agent.onGlobalCycleBegin();
		}
		for (Agent<? extends Amas<T>> agent : agents) {
			agent.cycle();
		}
		for (Agent<? extends Amas<T>> agent : agents) {
			agent.onGlobalCycleEnd();
		}
		onCycleEnd();
	}
	protected void onCycleEnd() {
		
	}
	protected void onCycleBegin() {
		
	}
	public final int getCycle() {
		return cycle;
	}
	public final T getEnvironment() {
		return environment;
	}
	public boolean stopCondition() {
		return false;
	}
	public List<Agent<? extends Amas<T>>> getAgents() {
		return agents;
	}
}
