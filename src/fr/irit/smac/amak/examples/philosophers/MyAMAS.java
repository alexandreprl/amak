package fr.irit.smac.amak.examples.philosophers;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Scheduling;
import fr.irit.smac.amak.ui.MainWindow;

public class MyAMAS extends Amas<TableEnvironment> {
	private JLabel comp;
	private Philosopher[] ps;

	public MyAMAS(TableEnvironment env) {
		super(env, Scheduling.UI);
	}

	@Override
	protected void onInitialConfiguration() {
		JToolBar toolbar = new JToolBar();
		comp = new JLabel("Cycle");
		comp.setPreferredSize(new Dimension(200, 100));
		toolbar.add(comp);
		MainWindow.addToolbar(toolbar);

		MainWindow.addTabbedPanel("tabbed1",new JPanel());
		MainWindow.addTabbedPanel("tabbed2",new JPanel());
		MainWindow.addTabbedPanel("tabbed3",new JPanel());
	}

	@Override
	protected void onInitialAgentsCreation() {
		ps = new Philosopher[getEnvironment().getForks().length];
		// Create one agent per fork
		for (int i = 0; i < getEnvironment().getForks().length - 1; i++) {
			ps[i] = new Philosopher(i, this, getEnvironment().getForks()[i], getEnvironment().getForks()[i + 1]);
		}

		// Let the last philosopher takes the first fork (round table)
		ps[getEnvironment().getForks().length - 1] = new Philosopher(getEnvironment().getForks().length - 1, this,
				getEnvironment().getForks()[getEnvironment().getForks().length - 1], getEnvironment().getForks()[0]);

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
