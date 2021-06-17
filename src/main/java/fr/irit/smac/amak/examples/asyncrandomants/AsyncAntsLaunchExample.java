package fr.irit.smac.amak.examples.asyncrandomants;

import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.irit.smac.amak.Configuration;
import fr.irit.smac.amak.Scheduling;
import fr.irit.smac.amak.ui.MainWindow;

/**
 * Class aiming at starting the mas-less ants system
 * 
 * @author perles
 *
 */
public class AsyncAntsLaunchExample {

	/**
	 * Launch method
	 * 
	 * @param args
	 *            Main arguments
	 */
	public static void main(String[] args) {
		Configuration.allowedSimultaneousAgentsExecution = 5;
		AsyncWorldExample env = new AsyncWorldExample();
		AsyncAntsAMASExample amas = new AsyncAntsAMASExample(env, Scheduling.UI);
		for (int i = 0; i < 50; i++)
			new AsyncAntExample(amas, 0, 0);
		JPanel panel = new JPanel();
		panel.add(new JLabel("<html><b>Async AntHill simulation</b><br/><br/>" + "Ants move randomly.<br />"
				+ "This demo is here to show AMAK<br />asynchronous agent capacities.</html>"));
		MainWindow.setLeftPanel(panel);
	}
}
