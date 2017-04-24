package fr.irit.smac.amak.examples.philosopherslinks;

import java.util.Random;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.lxplot.LxPlot;
import fr.irit.smac.lxplot.commons.ChartType;

public class Philosopher extends Agent<MyAMAS, TableEnvironment> {

	private Fork left;
	private Fork right;
	private double hungerDuration;
	private double eatenPastas;
	private int id;

	public enum State {
		THINK, HUNGRY, EATING
	}

	private State state = State.THINK;

	public Philosopher(int id, MyAMAS amas, Fork left, Fork right) {
		super(amas);
		this.id = id;
		this.left = left;
		this.right = right;
	}

	@Override
	protected void onPerceiveDecideAct() {
		State nextState = state;
		switch (state) {
		case EATING:
			eatenPastas++;
			if (new Random().nextInt(101) > 50) {
				left.release(this);
				right.release(this);
				nextState = State.THINK;
			}
			break;
		case HUNGRY:
			hungerDuration++;
			if (getMostCriticalNeighbor(true) == this) {
				if (left.tryTake(this) && right.tryTake(this))
					nextState = State.EATING;
				else{
					left.release(this);
					right.release(this);
				}
			} else {
				left.release(this);
				right.release(this);
			}
			break;
		case THINK:
			if (new Random().nextInt(101) >50) {
				hungerDuration = 0;
				nextState = State.HUNGRY;
			}
			break;
		default:
			break;

		}

		state = nextState;
	}

	@Override
	protected double computeCriticality() {
		return hungerDuration;
	}

	@Override
	protected void onDraw() {
		LxPlot.getChart("Eaten Pastas", ChartType.BAR).add(id, eatenPastas);
		amas.getCurrentSnapshot().addEntity(""+hashCode(), "Philosopher");
//		for (Agent<MyAMAS, TableEnvironment> agent : neighborhood) {
//			
//		}
	}
	
}
