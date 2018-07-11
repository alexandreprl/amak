package fr.irit.smac.amak.examples.asyncrandomants;

import javax.swing.JLabel;
import javax.swing.JPanel;

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
		AsyncWorldExample env = new AsyncWorldExample();

		for (int i = 0; i < 50; i++)
			new AsyncAntExample(env, 0, 0);
		JPanel panel = new JPanel();
		panel.add(new JLabel("<html><b>Async AntHill simulation</b><br/><br/>" + "Ants move randomly.<br />"
				+ "This demo is here to show AMAK<br />asynchronous agent capacities.</html>"));
		MainWindow.setLeftPanel("", panel);
	}
}
