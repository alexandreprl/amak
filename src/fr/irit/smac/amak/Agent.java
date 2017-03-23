package fr.irit.smac.amak;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class Agent<T extends Amas> {
	protected List<Agent<T>> neighborhood;
	protected Map<Agent<T>, Double> criticalities = new HashMap<>();
	private double criticality;
	protected T amas;

	public Agent(T amas) {
		this.amas = amas;
		this.amas.addAgent(this);
		neighborhood = new ArrayList<>();
		neighborhood.add(this);
		onInitialize();
	}

	@SuppressWarnings("unchecked")
	public final void addNeighbor(Agent<T>... agents) {
		for (Agent<T> agent : agents) {
			neighborhood.add(agent);
			criticalities.put(agent, Double.NEGATIVE_INFINITY);
		}
	}

	protected double computeCriticality() {
		return Double.NEGATIVE_INFINITY;
	}

	protected void onPerceive() {

	}

	protected void onDecide() {

	}

	protected void onAct() {

	}

	protected void onExpose() {

	}
	protected void onDraw() {
		
	}

	/**
	 * Called when all agents have been created and are ready to be started
	 */
	protected void onReady() {

	}

	/**
	 * Called before all agents are created
	 */
	protected void onInitialize() {

	}


	protected void onGlobalCycleBegin() {

	}

	protected void onGlobalCycleEnd() {

	}

	public final void cycle() {
		for (Agent<T> agent : neighborhood) {
			criticalities.put(agent, agent.criticality);
		}
		onPerceiveDecideAct();
		criticality = computeCriticality();
		onExpose();
		onDraw();
	}

	protected void onPerceiveDecideAct() {
		onPerceive();
		onDecide();
		onAct();
	}

	protected final Agent<T> getMostCriticalNeighbor(boolean includingMe) {
		List<Agent<T>> criticalest = new ArrayList<>();
		double maxCriticality = Double.NEGATIVE_INFINITY;

		if (includingMe) {
			criticalest.add(this);
			maxCriticality = criticalities.getOrDefault(criticalest, Double.NEGATIVE_INFINITY);
		}
		for (Entry<Agent<T>, Double> e : criticalities.entrySet()) {
			if (e.getValue() > maxCriticality) {
				criticalest.clear();
				maxCriticality = e.getValue();
				criticalest.add(e.getKey());
			}else if (e.getValue()==maxCriticality) {
				criticalest.add(e.getKey());
			}
		}
		if (criticalest.isEmpty())
			return null;
		return criticalest.get((int) (Math.random()*criticalest.size()));
	}
}
