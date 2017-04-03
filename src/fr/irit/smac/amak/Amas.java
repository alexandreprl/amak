package fr.irit.smac.amak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.irit.smac.amak.ui.SchedulerToolbar;
import fr.irit.smac.amak.ui.ToolbarWindow;


public class Amas<E extends Environment> {

	protected List<Agent<?,E>> agents = new ArrayList<>();
	protected int cycle;
	protected E environment;
	private ToolbarWindow toolbarWindow;
	private Scheduler scheduler;

	public Amas(E env) {
		this.environment = env;
		this.onInitialAgentsCreation();
		for (Agent<?,E> agent : agents) {
			agent.onReady();
		}
		this.scheduler = new Scheduler(this, false);
		toolbarWindow = new ToolbarWindow();
		toolbarWindow.addToolbar(new SchedulerToolbar(this.scheduler));
	}
	public ToolbarWindow getToolbarWindow() {
		return toolbarWindow;
	}
	protected void onInitialAgentsCreation() {
	}
	public final void _addAgent(Agent<?,E> _agent) {
		agents.add(_agent);
	}
	public final void cycle() {
		cycle++;
		Collections.shuffle(agents);
		onSystemCycleBegin();
		for (Agent<?,E> agent : agents) {
			agent.onSystemCycleBegin();
		}
		for (Agent<?,E> agent : agents) {
			agent.cycle();
		}
		for (Agent<?,E> agent : agents) {
			agent.onSystemCycleEnd();
		}
		onSystemCycleEnd();
	}
	protected void onSystemCycleEnd() {
		
	}
	protected void onSystemCycleBegin() {
		
	}
	public final int getCycle() {
		return cycle;
	}
	public final E getEnvironment() {
		return environment;
	}
	public boolean stopCondition() {
		return false;
	}
	public List<Agent<?,E>> getAgents() {
		return agents;
	}
}
