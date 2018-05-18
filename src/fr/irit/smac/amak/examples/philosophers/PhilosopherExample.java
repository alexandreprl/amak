package fr.irit.smac.amak.examples.philosophers;

import java.awt.Color;
import java.util.Random;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.ui.VUI;
import fr.irit.smac.amak.ui.drawables.Drawable;
import fr.irit.smac.amak.ui.drawables.DrawableRectangle;
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
	private DrawableRectangle drawableRectangle;
	private Drawable drawableLeftFork;
	private Drawable drawableRightFork;

	public PhilosopherExample(int id, PhilosophersAMASExample amas, ForkExample left, ForkExample right) {
		super(amas, id, left, right);
	}

	@Override
	protected void onInitialization() {
		this.id = (int) params[0];
		this.left = (ForkExample) params[1];
		this.right = (ForkExample) params[2];
	}

	@Override
	protected void onRenderingInitialization() {

		double x = 100 * Math.cos(2 * Math.PI * id / this.amas.getEnvironment().getForks().length);
		double y = 100 * Math.sin(2 * Math.PI * id / this.amas.getEnvironment().getForks().length);
		drawableRectangle = VUI.get().createRectangle(x, y, 20, 20);
		drawableLeftFork = VUI.get().createRectangle(x - 10, y, 5, 20).setColor(Color.black).setStrokeOnly().hide();
		drawableRightFork = VUI.get().createRectangle(x + 10, y, 5, 20).setColor(Color.black).setStrokeOnly().hide();

	}

	@Override
	protected void onPerceive() {
		// Nothing goes here as the perception of neighbors criticality is already made
		// by the framework
	}

	@Override
	protected void onDecideAndAct() {
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
				left.tryTake(this);
				right.tryTake(this);
				if (left.owned(this) && right.owned(this))
					nextState = State.EATING;

			} else {
				left.release(this);
				right.release(this);
			}
			break;
		case THINK:
			if (new Random().nextInt(101) > 50) {
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
		if (state == State.HUNGRY)
			return hungerDuration;
		return -1;
	}

	@Override
	protected void onUpdateRender() {
		LxPlot.getChart("Eaten Pastas", ChartType.BAR).add(id, eatenPastas);
		switch (state) {
		case EATING:
			drawableRectangle.setColor(Color.BLUE);
			break;
		case HUNGRY:
			drawableRectangle.setColor(Color.RED);
			break;
		case THINK:
			drawableRectangle.setColor(Color.GREEN);
			break;

		}
		if (left.owned(this)) {
			drawableLeftFork.show();
		} else {
			drawableLeftFork.hide();
		}
		if (right.owned(this)) {
			drawableRightFork.show();
		} else {
			drawableRightFork.hide();
		}
	}
}
