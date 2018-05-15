package fr.irit.smac.amak.examples.randomants;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.irit.smac.amak.ui.MainWindow;

public class AntsLaunchExample {

	public static void main(String[] args) {
		WorldExample env = new WorldExample();
		
		AntHillExample antHill = new AntHillExample(env);
		
		JPanel panel = new JPanel();
		panel.add(new JLabel("<html><b>AntHill simulation</b><br/><br/>"
				+ "Ants act move randomly.<br />"
				+ "This demo is here to show AMAK<br />rendering capacities.</html>"));
		MainWindow.setLeftPanel("", panel);
	}
}
