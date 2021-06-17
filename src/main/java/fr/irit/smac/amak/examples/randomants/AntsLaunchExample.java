package fr.irit.smac.amak.examples.randomants;

import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.irit.smac.amak.ui.MainWindow;

public class AntsLaunchExample {

	public static void main(String[] args) {
		WorldExample env = new WorldExample();
		
		new AntHillExample(env);
		
		JPanel panel = new JPanel();
		panel.add(new JLabel("<html><b>AntHill simulation</b><br/><br/>"
				+ "Ants move randomly.<br />"
				+ "This demo is here to show AMAK<br />rendering capacities.</html>"));
		MainWindow.setLeftPanel(panel);
	}
}
