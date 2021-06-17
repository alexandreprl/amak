package fr.irit.smac.amak.examples.philosophers;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JToolBar;

import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Configuration;
import fr.irit.smac.amak.Scheduling;
import fr.irit.smac.amak.ui.MainWindow;
import fr.irit.smac.amak.ui.VUI;

public class PhilosophersAMASExample extends Amas<TableExample> {
	private JLabel comp;
	private PhilosopherExample[] ps;

	public PhilosophersAMASExample(TableExample env) {
		super(env, Scheduling.DEFAULT);
	}
	
	@Override
	protected void onInitialConfiguration() {
		Configuration.executionPolicy = ExecutionPolicy.TWO_PHASES;
		JToolBar toolbar = new JToolBar();
		comp = new JLabel("Cycle");
		comp.setPreferredSize(new Dimension(200, 100));
		toolbar.add(comp);
		MainWindow.addToolbar(toolbar);
		VUI.get().createRectangle(55, 45, 110, 90).setColor(new Color(0.9f, 0.9f, 0.9f, 0.5f)).setFixed().setLayer(5);

		VUI.get().createRectangle(20, 20, 20, 20).setColor(Color.red).setFixed().setLayer(10);
		VUI.get().createString(45, 25, "Hungry").setFixed().setLayer(10);

		VUI.get().createRectangle(20, 45, 20, 20).setColor(Color.blue).setFixed().setLayer(10);
		VUI.get().createString(45, 50, "Eating").setFixed().setLayer(10);

		VUI.get().createRectangle(20, 70, 20, 20).setColor(Color.green).setFixed().setLayer(10);
		VUI.get().createString(45, 75, "Thinking").setFixed().setLayer(10);
	}

	@Override
	protected void onInitialAgentsCreation() {
		ps = new PhilosopherExample[getEnvironment().getForks().length];
		// Create one agent per fork
		for (int i = 0; i < getEnvironment().getForks().length - 1; i++) {
			ps[i] = new PhilosopherExample(i, this, getEnvironment().getForks()[i], getEnvironment().getForks()[i + 1]);
		}

		// Let the last philosopher takes the first fork (round table)
		ps[getEnvironment().getForks().length - 1] = new PhilosopherExample(getEnvironment().getForks().length - 1,
				this, getEnvironment().getForks()[getEnvironment().getForks().length - 1],
				getEnvironment().getForks()[0]);

		// Add neighborhood
		for (int i = 1; i < ps.length; i++) {
			ps[i].addNeighbor(ps[i - 1]);
			ps[i - 1].addNeighbor(ps[i]);
		}
		ps[0].addNeighbor(ps[ps.length - 1]);
		ps[ps.length - 1].addNeighbor(ps[0]);
	}

	@Override
	protected void onSystemCycleBegin() {
		comp.setText("Cycle " + getCycle());
	}
}
