package fr.irit.smac.amak.examples.philosophers;

import java.util.Random;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.lxplot.LxPlot;
import fr.irit.smac.lxplot.commons.ChartType;

public class PhilosopherExample extends Agent<PhilosophersAMASExample, TableExample> {

	private ForkExample left;
	private ForkExample right;
	private double hungerDuration;
	private double eatenPastas;
	private int id;

	public enum State {
		THINK, HUNGRY, EATING
	}

	private State state = State.THINK;

	public PhilosopherExample(int id, PhilosophersAMASExample amas, ForkExample left, ForkExample right) {
		super(amas,id,left,right);
	}
	@Override
	protected void onInitialize() {
		this.id = (int) params[0];
		this.left = (ForkExample) params[1];
		this.right = (ForkExample) params[2];
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
	}
}
