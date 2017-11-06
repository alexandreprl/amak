package fr.irit.smac.amak.examples.randomants;

public class AntsLaunchExample {

	public static void main(String[] args) {
		WorldExample env = new WorldExample();
		
		AntHillExample antHill = new AntHillExample(env);
		new AntViewerExample(antHill);
	}
}
