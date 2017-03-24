package fr.irit.smac.amak;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class Agent<A extends Amas<E>, E extends Environment> {
	protected List<Agent<A,E>> neighborhood;
	protected Map<Agent<A,E>, Double> criticalities = new HashMap<>();
	private double criticality;
	protected A amas;

	public Agent(A amas) {
		this.amas = amas;
		this.amas._addAgent(this);
		neighborhood = new ArrayList<>();
		neighborhood.add(this);
		onInitialize();
	}

	@SuppressWarnings("unchecked")
	public final void addNeighbor(Agent<A,E>... agents) {
		for (Agent<A,E> agent : agents) {
			neighborhood.add(agent);
			criticalities.put(agent, Double.NEGATIVE_INFINITY);
		}
	}

	protected double computeCriticality() {
		return Double.NEGATIVE_INFINITY;
	}
	protected void onAgentCycleBegin() {
		
	}
	protected void onAgentCycleEnd() {
		
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
	 * Called when all initial agents have been created and are ready to be started
	 */
	protected void onReady() {

	}

	/**
	 * Called before all agents are created
	 */
	protected void onInitialize() {

	}


	protected void onSystemCycleBegin() {

	}

	protected void onSystemCycleEnd() {

	}

	public final void cycle() {
		onAgentCycleBegin();
		for (Agent<A,E> agent : neighborhood) {
			criticalities.put(agent, agent.criticality);
		}
		onPerceiveDecideAct();
		criticality = computeCriticality();
		onExpose();
		onDraw();
		onAgentCycleEnd();
	}

	protected void onPerceiveDecideAct() {
		onPerceive();
		onDecide();
		onAct();
	}

	protected final Agent<A,E> getMostCriticalNeighbor(boolean includingMe) {
		List<Agent<A,E>> criticalest = new ArrayList<>();
		double maxCriticality = Double.NEGATIVE_INFINITY;

		if (includingMe) {
			criticalest.add(this);
			maxCriticality = criticalities.getOrDefault(criticalest, Double.NEGATIVE_INFINITY);
		}
		for (Entry<Agent<A,E>, Double> e : criticalities.entrySet()) {
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
